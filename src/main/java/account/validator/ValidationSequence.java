package account.validator;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Default.class, EnumValidationGroup.class})
public interface ValidationSequence {
}
