import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserListService } from 'src/app/services/user-list.service';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
})
export class AddUserComponent {
  userForm: FormGroup;

  constructor(private fb: FormBuilder, private userListService: UserListService) {
    this.userForm =
      this.fb.group(
        {
          name: ['', Validators.required],
          surname: ['', Validators.required],
          email: ['', [Validators.required, Validators.email],],
          password: ['', [Validators.required, Validators.minLength(8)],],
          permission:
            this.fb.group({
              read: [false],
              create: [false],
              update: [false],
              delete: [false],
            },
            )
        }
      );
  }

  onSubmit() {
    if (this.userForm.valid) {
      this.userListService.addNew(this.userForm)
    } else {
      alert('Form is invalid. Please check the fields.');
    }
  }

  get f() {
    return this.userForm.controls;
  }
}

