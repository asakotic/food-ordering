import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient, private router: Router) { }

  send(email:string,password:string) {
    const loginUrl = 'http://localhost:8080/users/login';

    this.http.post(loginUrl, { email:email, password:password } ).subscribe({
      next: (response: any) => {
        console.log(response);
        
        if (response.jwt) {
          localStorage.setItem('jwt', response.jwt);
          this.router.navigate(['/user-list']);
        } else {
          alert('Token not found.');
        }
      },
      error: (err) => {
        if (err.status === 403) {
          alert('Bad login');
        } else {
          alert('Error: There was mistake.');
        }
      }
    });
  }
}
