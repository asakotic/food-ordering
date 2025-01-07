import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { User } from 'src/app/models/user';
import { UserListService } from 'src/app/services/user-list.service';

@Component({
  selector: 'app-update-user',
  templateUrl: './update-user.component.html',
})
export class UpdateUserComponent implements OnInit {
  userForm: FormGroup;

  constructor(private fb: FormBuilder, private userListService: UserListService, private ar: ActivatedRoute) {
    this.userForm =
      this.fb.group(
        {
          id:'',
          name: ['', Validators.required],
          surname: ['', Validators.required],
          email: ['', [Validators.required, Validators.email],],
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

  ngOnInit(): void {
    const em = this.ar.paramMap.subscribe(res => {
      const id = res.get("id");
      if (id)
        this.userListService.fetchUser(Number(id)).subscribe
          (result => {
            const user = {
              id:result.id,
              name: result.name,
              surname: result.surname,
              email: result.email,
              permission: result.permission
            }
            this.userForm.setValue(user)
          }
          )
    })
  }


  onSubmit() {
    if (this.userForm.valid) {
      this.userListService.updateUser(this.userForm)
    } else {
      alert('Form is invalid. Please check the fields.');
    }
    
  }

  get f() {
    return this.userForm.controls;
  }
}


