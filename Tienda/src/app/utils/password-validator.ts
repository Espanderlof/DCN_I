export class PasswordValidator {
    static validate(password: string): { isValid: boolean; errors: string[] } {
        const errors: string[] = [];

        // 1. Longitud mínima de 8 caracteres
        if (password.length < 8) {
            errors.push('La contraseña debe tener al menos 8 caracteres');
        }

        // 2. Longitud máxima de 20 caracteres
        if (password.length > 20) {
            errors.push('La contraseña no debe exceder los 20 caracteres');
        }

        // 3. Al menos una letra mayúscula
        if (!/[A-Z]/.test(password)) {
            errors.push('La contraseña debe contener al menos una letra mayúscula');
        }

        // 4. Al menos una letra minúscula
        if (!/[a-z]/.test(password)) {
            errors.push('La contraseña debe contener al menos una letra minúscula');
        }

        // 5. Al menos un número
        if (!/\d/.test(password)) {
            errors.push('La contraseña debe contener al menos un número');
        }

        // 6. Al menos un carácter especial
        if (!/[!@#$%^&*(),.?":{}|<>]/.test(password)) {
            errors.push('La contraseña debe contener al menos un carácter especial (!@#$%^&*(),.?":{}|<>)');
        }

        return {
            isValid: errors.length === 0,
            errors
        };
    }
}