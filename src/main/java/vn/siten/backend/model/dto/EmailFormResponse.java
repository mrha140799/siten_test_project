package vn.siten.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailFormResponse {
    private String emailAddressTo;
    private String subject;
    private String text;
}
