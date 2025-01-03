import { PasswordValidator } from './password-validator';

describe('PasswordValidator', () => {
  describe('validaciones basicas', () => {
    it('deberia validar una contrasena que cumple todos los requisitos', () => {
      const resultado = PasswordValidator.validate('Test123!@');
      expect(resultado.isValid).toBe(true);
      expect(resultado.errors).toEqual([]);
    });

    it('deberia rechazar una contrasena vacia', () => {
      const resultado = PasswordValidator.validate('');
      expect(resultado.isValid).toBe(false);
      expect(resultado.errors).toContain('La contraseña debe tener al menos 8 caracteres');
    });
  });

  describe('validacion de longitud', () => {
    it('deberia rechazar una contrasena muy corta', () => {
      const resultado = PasswordValidator.validate('Abc1!');
      expect(resultado.isValid).toBe(false);
      expect(resultado.errors).toContain('La contraseña debe tener al menos 8 caracteres');
    });

    it('deberia rechazar una contrasena muy larga', () => {
      const resultado = PasswordValidator.validate('Abcd1234!@#$%^&*()12345678');
      expect(resultado.isValid).toBe(false);
      expect(resultado.errors).toContain('La contraseña no debe exceder los 20 caracteres');
    });
  });

  describe('validacion de caracteres', () => {
    it('deberia detectar la falta de mayusculas', () => {
      const resultado = PasswordValidator.validate('test123!@');
      expect(resultado.isValid).toBe(false);
      expect(resultado.errors).toContain('La contraseña debe contener al menos una letra mayúscula');
    });

    it('deberia detectar la falta de minusculas', () => {
      const resultado = PasswordValidator.validate('TEST123!@');
      expect(resultado.isValid).toBe(false);
      expect(resultado.errors).toContain('La contraseña debe contener al menos una letra minúscula');
    });

    it('deberia detectar la falta de numeros', () => {
      const resultado = PasswordValidator.validate('TestTest!@');
      expect(resultado.isValid).toBe(false);
      expect(resultado.errors).toContain('La contraseña debe contener al menos un número');
    });

    it('deberia detectar la falta de caracteres especiales', () => {
      const resultado = PasswordValidator.validate('TestTest123');
      expect(resultado.isValid).toBe(false);
      expect(resultado.errors).toContain('La contraseña debe contener al menos un carácter especial (!@#$%^&*(),.?":{}|<>)');
    });
  });

});