import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Dish2 } from '../models/dish';

@Injectable({
  providedIn: 'root',
})
export class CreateOrderService {

  constructor(private http: HttpClient) { }

  createOrder(orderData: any, token: string): Observable<any> {
    const url = 'http://localhost:8080/orders/create'
    const headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.http.post(url, orderData, { headers });
  }

  scheduleOrder(orderItems: any[], token: string, scheduledTime: string,): Observable<any> {
    const url = 'http://localhost:8080/orders/schedule';
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    });

    const requestBody = {
      scheduledTime: scheduledTime, 
      dishOrderDtos: orderItems,   
    };

    return this.http.post(url, requestBody, { headers });
  }

  getDishes(): Observable<Dish2[]> {
    const token = localStorage.getItem('jwt');
    const url = 'http://localhost:8080/orders/dishes';
    const headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.http.get<Dish2[]>(url, { headers });
  }
}
