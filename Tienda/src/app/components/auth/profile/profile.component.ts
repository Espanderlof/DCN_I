import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { User } from '../../../models/user';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  user: User | null = null;
  editMode = false;
  successMessage = '';
  errorMessage = '';
  editableUser: Partial<User> = {};

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      if (!user) {
        this.router.navigate(['/login']);
        return;
      }
      this.user = user;
      this.editableUser = { ...user };
    });
  }

  toggleEditMode(): void {
    this.editMode = !this.editMode;
    if (!this.editMode) {
      this.editableUser = { ...this.user };
    }
    this.successMessage = '';
    this.errorMessage = '';
  }

  onSubmit(): void {
    if (!this.user || !this.editableUser) return;

    const updatedUser: User = {
      ...this.user,
      ...this.editableUser,
      ultimaActualizacion: new Date()
    };

    this.authService.updateProfile(updatedUser)
      .subscribe({
        next: (user) => {
          this.user = user;
          this.editMode = false;
          this.successMessage = 'Perfil actualizado correctamente';
          this.errorMessage = '';
        },
        error: () => {
          this.errorMessage = 'Error al actualizar el perfil';
          this.successMessage = '';
        }
      });
  }
}