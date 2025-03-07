import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/models/user';
import { UserListService } from 'src/app/services/user-list.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  can_read: boolean = false;
  can_create: boolean = false;
  can_update: boolean = false;
  can_delete: boolean = false;
  JSON = JSON

  constructor(private service: UserListService, private router:Router) { }

  ngOnInit() {
    this.checkPermissions();
    console.log(this.can_read)
    if (this.can_read) {
      this.fetchUsers();
    }
  }

  checkPermissions() {
    const token = localStorage.getItem('jwt');
    if (!token) {
      alert('You need to sign in!');
      this.router.navigate(['/login']);
      return;
    }
    const payload = JSON.parse(atob(token.split('.')[1]));

    this.can_read = payload.can_read || false;
    this.can_create = payload.can_create || false;
    this.can_update = payload.can_update || false;
    this.can_delete = payload.can_delete || false;
  }

  fetchUsers() {
      this.service.fetchUsers().subscribe((results) => {
        this.users = results
    });
  }

  editUser(id: number) {
    this.service.editUser(id)
  }

  deleteUser(email: string) {
      this.service.deleteUser(email)
  }
  addUser(can_create: boolean){
    if(can_create)
      this.router.navigate(['/add-user'])
  }

  formatPermissions(user: any): string {
    const permissions = [];
    if (user.permission.read) permissions.push('Read');
    if (user.permission.create) permissions.push('Create');
    if (user.permission.update) permissions.push('Update');
    if (user.permission.delete) permissions.push('Delete');
    if (user.permission.search) permissions.push('Search');
    if (user.permission.order) permissions.push('Order');
    if (user.permission.cancel) permissions.push('Cancel');
    if (user.permission.track) permissions.push('Track');
     if (user.permission.schedule) permissions.push('Schedule');
    return permissions.join(', ');
  }

}
