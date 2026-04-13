package com.example.CarDealerShip.Models;

import com.example.CarDealerShip.ConstraintValidators.DateConstraint;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter // CarWithDocsDTO is used as a parameter in the controller method when binding form data to the controller. It must have setters to allow values from the form to be assigned to its properties.
@AllArgsConstructor
@NoArgsConstructor
public class CarWithDocsDTO {

    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("yyyy MMM, dd");
    Integer itemNo;
    String make;
    String model;
    Integer year;
    
    @DateConstraint
    @Past
    @DateTimeFormat(pattern = "yyyy-MMM-dd", fallbackPatterns ={"yyyy-MM-dd","dd/MMM/yyyy","dd.MM.yyyy","MM/dd/yyyy","dd/MM/yyyy","yyyy/MM/dd","yyyy/dd/MM"})
    LocalDate datePurchased;
    
    Transmissions powerTrain;
    
    @Positive(message = "invalid price, should be a positive value")
    Double price;
    String condn;
    
    @PositiveOrZero(message = "invalid hp, should be a non-negative value")
    Double horsePower;
    Integer docId;
    String docName;
    Long docSize;

    /**
     * Formatting "LocalDate datePurchased" for display in JSP page.
     * Add a display field (or getter) on the DTO used by the view. Why this
     * approach is actually preferred? This is considered cleaner because:
     * - Formatting logic stays close to the data used by the view, meaning that
     *   the code that decides how data should be displayed should live near the
     *   object that the view is using, instead of being scattered across
     *   controllers or JSP pages. The JSP only cares about how to display. It 
     *   does not need to know how to format dates, what formatter to use, and  
     *   how to handle null values.
     * - JSP remains simple (no Java logic). 
     * - No extra controller code needed. 
     * - No session state required.
     * - Easy to reuse across pages.
     * So this way (add a display field (or getter) on the DTO), the DTO knows
     * how its data should be presented. The view stays simple. Format is
     * defined in one place and it's easy to change later.
     *  
     * What happens if formatting is in the JSP (less ideal)? Example:
     * In the JSP if we use the following:
     * ${carFirst.datePurchased.format(DateTimeFormatter.ofPattern('yyyy MMM, dd'))}
     * Now, formatting logic is inside the UI, every JSP must repeat it, if 
     * the format changes you have to update many pages, view contains Java logic
     * and this spreads formatting knowledge everywhere.
     * 
     * In JSP EL, this ${carFirst.datePurchasedDisplay} does not require an
     * actual field named datePurchasedDisplay. When JSP sees
     * ${carFirst.datePurchasedDisplay} it internally tries
     * carFirst.getDatePurchasedDisplay(), so if the DTO contains a getter
     * method then EL will call this method automatically. No field needed.
     */
    public String getDatePurchasedDisplay() {
        return datePurchased.format(DISPLAY_FORMAT);
    }
}
