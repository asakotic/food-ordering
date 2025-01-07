import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { User } from '../models/user';
import { Observable } from 'rxjs';
import { FormGroup } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class UserListService {

  constructor(private http:HttpClient, private router:Router) { }

  fetchUsers(): Observable<User[]>{
    const token = localStorage.getItem('jwt');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    const loginUrl = 'http://localhost:8080/users/all';

    return this.http.get<User[]>(loginUrl, { headers: headers })
  }

  fetchUser(id:number): Observable<User> {
    const token = localStorage.getItem('jwt');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    const loginUrl = `http://localhost:8080/users/getId/${id}`;

    return this.http.get<User>(loginUrl, { headers: headers })
  }

  editUser(id: number) {
      this.router.navigate(['/update-user', id])
  }
  updateUser(userForm:FormGroup){
    const token = localStorage.getItem('jwt');
    const headers = { Authorization: `Bearer ${token}` };
    let payload = null
    if (token)
      payload = JSON.parse(atob(token.split('.')[1]));

    let can_update = payload.can_update || false;
    if (can_update)
      this.http.put(`http://localhost:8080/users/update`, userForm.value, { headers }).subscribe({
        next: (response: any) => {
          if (response.jwt) {
            localStorage.setItem('jwt', response.jwt);
            this.router.navigate(['/user-list']);
          } else {
            alert('Token not found.');
          }
        },
        error: (err) => {
          console.error('Error:', err);
          alert("You don't have permission to edit users.");
        }
      });
  }
  addNew(userForm: FormGroup) {
    const token = localStorage.getItem('jwt');
    const headers = { Authorization: `Bearer ${token}` };
    let payload = null
    if (token)
      payload = JSON.parse(atob(token.split('.')[1]));

    let can_create = payload.can_create || false;
    if (can_create)
      this.http.post(`http://localhost:8080/users/create`, userForm.value, { headers }).subscribe({
        next: () => {
          this.router.navigate(['/user-list']);
        },
        error: (err) => {
          console.error('Error:', err);
          alert("You don't have permission to add users.");
        }
      });
  }
  deleteUser(email: string) {
    if (confirm('Delete this user?')) {
      const token = localStorage.getItem('jwt');
      const headers = { Authorization: `Bearer ${token}` };
      let payload = null
      if (token)
        payload = JSON.parse(atob(token.split('.')[1]));

      let can_delete = payload.can_delete || false;
      if (can_delete)
        this.http.delete(`http://localhost:8080/users/delete/${email}`, { headers }).subscribe({
          next: () => {
            document.location.reload()
          },
          error: (err) => {
            console.error('Error:', err);
            alert("You don't have permission to delete users.");
          }
        });
    }
  }

  


}
