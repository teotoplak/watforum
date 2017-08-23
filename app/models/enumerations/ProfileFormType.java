package models.enumerations;

/**
 * Created by teo on 8/23/17.
 * Enum to tell profile form how to act
 * Normal = not oauth
 * Reg = register
 */
public enum ProfileFormType {
    EDIT_OAUTH,
    EDIT_NORMAL,
    REG_OAUTH,
    REG_NORMAL
}
