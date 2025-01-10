import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ErrorServiceService {

  private apiUrl = 'http://localhost:8080/errors';

  constructor(private http: HttpClient) { }

  getErrors(page: number, size: number): Observable<any> {
    const token = localStorage.getItem('jwt') || '';
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<any>(`${this.apiUrl}?page=${page}&size=${size}`, {
      headers,
    });
  }
}
