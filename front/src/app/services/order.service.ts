import { Injectable } from '@angular/core';
import { HttpClient, HttpParams,HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Orderer } from '../models/orderer';


@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private apiUrl = 'http://localhost:8080/orders'; 

  constructor(private http: HttpClient) { }

  getAllOrders(token: string): Observable<Orderer[]> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.get<Orderer[]>(`${this.apiUrl}/search`, { headers });
  }

  searchOrders(filters: any, token: string): Observable<Orderer[]> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    let params = new HttpParams();
    //dodati proveru da li ima i date from i to inc ne dozvoliti
    if (filters.status) params = params.append('status', filters.status);
    if (filters.dateFrom) params = params.append('dateFrom', filters.dateFrom);
    if (filters.dateTo) params = params.append('dateTo', filters.dateTo);

    return this.http.get<Orderer[]>(`${this.apiUrl}/search`, { headers, params });
  }
  cancelOrder(orderId: number, token: string): Observable<void> {
    const headers = { Authorization: `Bearer ${token}` };
    return this.http.delete<void>(this.apiUrl+`/cancel/${orderId}`, { headers });
  }
}
