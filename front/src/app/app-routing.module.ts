import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './auth/auth.guard';
import { LoginComponent } from './components/login/login.component';
import { UserListComponent } from './components/user-list/user-list.component';
import { AddUserComponent } from './components/add-user/add-user.component';
import { UpdateUserComponent } from './components/update-user/update-user.component';

const routes: Routes = [
  {
    path: "",
    component: LoginComponent,
  },
  {
    path: "user-list",
    component: UserListComponent,
    canActivate : [AuthGuard]
  },
  {
    path: "add-user",
    component: AddUserComponent,
    canActivate : [AuthGuard]
  },
  {
    path: "update-user/:id",
    component: UpdateUserComponent,
    canActivate: [AuthGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
