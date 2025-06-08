package dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LinkFullDTO {
    private String link;

    private String original;

    private Long rank;

    private Long count;
}
