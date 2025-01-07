import { Component } from '@angular/core';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  email: string;
  password: string;

  constructor(private loginService: LoginService) {
    this.email = '';
    this.password = '';
  }

  send() {
    this.loginService.send(this.email,this.password);
  }

}
