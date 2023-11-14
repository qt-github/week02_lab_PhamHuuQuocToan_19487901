package vn.edu.iuh.fit.backend.converts;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import vn.edu.iuh.fit.backend.enums.EmployeeStatus;


@Converter(autoApply = true)
public class EmployeeStatusConvert implements AttributeConverter<EmployeeStatus,Integer> {

    @Override
    public Integer convertToDatabaseColumn(EmployeeStatus attribute) {
        if(attribute==null)
            return null;
        return  attribute.getCode();
    }

    @Override
    public EmployeeStatus convertToEntityAttribute(Integer dbData) {
        if(dbData==null)
            return null;
        return EmployeeStatus.fromcode(dbData);
    }
}