package vn.siten.backend.service.user.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.siten.backend.model.PointId;
import vn.siten.backend.model.bean.ResponseErrorBean;
import vn.siten.backend.model.bean.ResponseSuccessBean;
import vn.siten.backend.model.entity.*;
import vn.siten.backend.model.entity.Point;
import vn.siten.backend.model.bean.ResponseBean;
import vn.siten.backend.model.dto.PointFormRequest;
import vn.siten.backend.model.dto.StudentFormRequest;
import vn.siten.backend.model.util.MessageUtil;
import vn.siten.backend.repository.*;
import vn.siten.backend.service.FileUpload;
import vn.siten.backend.service.jwt.JwtProvider;
import vn.siten.backend.service.user.TeacherService;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    private static final Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);

    @Value("${url.path}")
    private String urlPath;
    @Value("${file.uploadDir}")
    private String UPLOAD_DIR;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private FileUpload fileUpload;
    @Autowired
    private MessageUtil messageUtil;
    private final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");


    @Override
    public ResponseBean createStudent(String jsonStudentFormStr, MultipartFile avatarPhoto) throws ParseException, JsonProcessingException {
        try {
            String avatarPhotoType = avatarPhoto.getContentType().split("/")[0];
            if (avatarPhoto == null || !avatarPhotoType.equals("image"))
                return new ResponseErrorBean(messageUtil.getMessageDefaultEmpty("error.avatar.malformed"));
            ObjectMapper objectMapper = new ObjectMapper();
            StudentFormRequest studentFormRequest = objectMapper.readValue(jsonStudentFormStr, StudentFormRequest.class);
            if (!facultyRepository.existsById(studentFormRequest.getFacultyId()))
                return new ResponseErrorBean(messageUtil.getMessageDefaultEmpty("error.faculty.id.not.exist"));
            String fileName = (new Date().getTime()) + "-" + avatarPhoto.getOriginalFilename();
            String path = UPLOAD_DIR + fileName;
            fileUpload.saveFile(avatarPhoto.getInputStream(), path);
            Date studentBirthDate = SIMPLE_DATE_FORMAT.parse(studentFormRequest.getBirthDate());
            Faculty faculty = facultyRepository.findById(studentFormRequest.getFacultyId()).get();
            Student student = new Student(studentFormRequest.getFullname(), studentBirthDate, studentFormRequest.getIdCard(), studentFormRequest.getAddress(), faculty, studentFormRequest.getIsMale(), studentFormRequest.getClassname());
            student.setAvatarName(fileName);
            studentRepository.save(student);
            studentFormRequest.setId(student.getId());
            studentFormRequest.setAvatarPhotoPath(urlPath + UPLOAD_DIR.replace(".", "") + fileName);
            return new ResponseSuccessBean(studentFormRequest);
        } catch (Exception e) {
            logger.error("CAN NOT CREATE STUDENT: " + e);
            return new ResponseErrorBean(e.getMessage());
        }
    }

    @Override
    public ResponseBean addPoint(PointFormRequest pointFormRequest) {
        if (!subjectRepository.existsById(pointFormRequest.getSubjectId()))
            return new ResponseErrorBean(messageUtil.getMessageDefaultEmpty("error.subject.id.not.exist"));
        if (!studentRepository.existsById(pointFormRequest.getStudentId()))
            return new ResponseErrorBean(messageUtil.getMessageDefaultEmpty("error.student.id.not.exist"));
        try {
            Student student = studentRepository.findById(pointFormRequest.getStudentId()).get();
            Subject subject = subjectRepository.findById(pointFormRequest.getSubjectId()).get();
            PointId pointid = new PointId(subject, student);
            Point point = new Point(pointid, pointFormRequest.getPoint());
            pointRepository.save(point);
            return new ResponseSuccessBean();
        } catch (Exception e) {
            return new ResponseErrorBean(e.getMessage());
        }
    }

    @Override
    public ResponseBean updateStudent(Long id, StudentFormRequest studentFormRequest) {
        try {
            if (!facultyRepository.existsById(studentFormRequest.getFacultyId()))
                return new ResponseErrorBean(messageUtil.getMessageDefaultEmpty("error.faculty.id.not.exist"));
            if (!studentRepository.existsById(id))
                return new ResponseErrorBean(messageUtil.getMessageDefaultEmpty("error.student.id.not.exist"));
            Date birthDate = SIMPLE_DATE_FORMAT.parse(studentFormRequest.getBirthDate());
            Faculty faculty = facultyRepository.findById(studentFormRequest.getFacultyId()).orElseThrow(() -> new RuntimeException("CAN NOT FOUNT FACULTY ID!"));
            Student student = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("CAN NOT FOUNT STUDENT ID"));
            student.setFullname(studentFormRequest.getFullname());
            student.setBirthDate(birthDate);
            student.setAddress(studentFormRequest.getAddress());
            student.setIdCard(studentFormRequest.getIdCard());
            student.setMale(studentFormRequest.getIsMale());
            student.setFaculty(faculty);
            studentRepository.save(student);
            return new ResponseSuccessBean();
        } catch (Exception e) {
            return new ResponseErrorBean(e.getMessage());
        }
    }

    @Override
    public ResponseBean deleteStudent(Long id) {
        if (!studentRepository.existsById(id))
            return new ResponseErrorBean(messageUtil.getMessageDefaultEmpty("error.student.id.not.exist"));
        Student student = studentRepository.findById(id).get();
        if (!student.getAvatarName().isEmpty()) {
            File file = new File(UPLOAD_DIR + student.getAvatarName());
            if (file.delete()) logger.info("Delete Avatar image: " +student.getAvatarName());
            else logger.error("Delete avatar image: " +student.getAvatarName() +"-Fail!");
        }
        studentRepository.deleteById(id);
        return new ResponseSuccessBean();
    }

    @Override
    public List<StudentFormRequest> getListStudent(String jwt, Pageable pageable) {
        String username = jwtProvider.getUserNameFromJwtToken(jwt.replace("Bearer ", ""));
        Teacher teacher = teacherRepository.findByAccount_Username(username).get();
        Iterable<Student> listStudent = studentRepository.findStudentByIdAndClassName(null, teacher.getClassName(), pageable);
        return convertListStudentToListStudentFormRequest(listStudent);
    }

    @Override
    public ResponseBean uploadExcelStudentToDatabase(MultipartFile excelFile) throws IOException {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);
            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                Student student = new Student();
                XSSFRow rows = worksheet.getRow(i);
                student.setFullname(rows.getCell(0).getStringCellValue());
                Date birthDate = SIMPLE_DATE_FORMAT.parse(rows.getCell(1).getStringCellValue());
                student.setBirthDate(birthDate);
                student.setIdCard(rows.getCell(2).getStringCellValue());
                student.setAddress(rows.getCell(3).getStringCellValue());
                Faculty faculty = facultyRepository.findById((long) rows.getCell(4).getNumericCellValue()).get();
                student.setFaculty(faculty);
                student.setMale(rows.getCell(5).getBooleanCellValue());
                student.setClassName(rows.getCell(6).getStringCellValue());
                studentRepository.save(student);
            }
            return new ResponseSuccessBean(studentRepository.findAll());

        } catch (Exception e) {
            return new ResponseErrorBean(e.getMessage());
        }
    }

    private List<StudentFormRequest> convertListStudentToListStudentFormRequest(Iterable<Student> students) {
        List<StudentFormRequest> listStudentFormRequest = new ArrayList<>();
        for (Student student : students) {
            String date = SIMPLE_DATE_FORMAT.format(student.getBirthDate());
            String path = urlPath + UPLOAD_DIR.replace(".", "");
            String avatarPhotoPath = path + student.getAvatarName();
            StudentFormRequest studentFormRequest = new StudentFormRequest(student.getId(), student.getFullname(), date, student.getIdCard(), student.getAddress(), student.getFaculty().getId(), student.isMale(), student.getClassName(), avatarPhotoPath);
            listStudentFormRequest.add(studentFormRequest);
        }
        return listStudentFormRequest;
    }

}
