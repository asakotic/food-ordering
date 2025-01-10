import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { OrderService } from 'src/app/services/order.service';
import { Orderer } from 'src/app/models/orderer';
import { UserListService } from 'src/app/services/user-list.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
})
export class OrderListComponent implements OnInit, OnDestroy {
  orders: Orderer[] = [];
  searchForm: FormGroup;
  token = localStorage.getItem('jwt') || "";
  statuses = ['ORDERED', 'PREPARING', 'IN_DELIVERY', 'DELIVERED', 'CANCELED','SCHEDULED'];
  private intervalId: any;
  isAdmin: boolean = false;
  can_search = false;  
  can_cancel = false;
  can_track = false;

  constructor(private orderService: OrderService, private fb: FormBuilder, private userService: UserListService,private router:Router) {
    this.searchForm = this.fb.group({
      status: [''],
      dateFrom: [''],
      dateTo: [''],
      userId: ['']
    });
  }

  ngOnInit(): void {
    this.checkPermissions();
    this.loadAllOrders();
    this.startAutoSearch();
  }

  ngOnDestroy(): void {
    if (this.intervalId) {
      clearInterval(this.intervalId); 
    }
  }

  loadAllOrders(): void {
    if (!this.can_search) {
      alert("u dont have permission")
      this.router.navigate(["/error-message"]);
      return; 
    }
    this.orderService.getAllOrders(this.token).subscribe(
      (orders) => {
        this.orders = orders;
        this.checkAdminStatus()
      },
      (error) => {
        alert(error)
      }
    );
  }

  onSearch(): void {
    if (!this.can_search) {
      alert('You do not have permission to search.');
      return;
    }
    const filters = this.searchForm.value;
    this.orderService.searchOrders(filters, this.token).subscribe(
      (orders) => {
        this.orders = orders;
      },
      (error) => {
          console.log(error);
      }
    );
  }

  startAutoSearch(): void {
    this.intervalId = setInterval(() => {
      this.loadAllOrders(); 
    }, 5000);
  }

  checkAdminStatus(): void {
    this.isAdmin = this.userService.checkAdminStatus()
  }

  cancelOrder(orderId: number): void {
    if (!this.can_cancel) {
      alert('You do not have permission to cancel orders.');
      return;
    }
    this.orderService.cancelOrder(orderId, this.token).subscribe(
      () => {
        this.loadAllOrders(); 
      },
      (error) => {
        this.loadAllOrders(); 
      }
    );
  }
  checkPermissions() {
    const token = localStorage.getItem('jwt');
    if (!token) {
      alert('You need to sign in!');
      this.router.navigate(['/login']);
      return;
    }
    const payload = JSON.parse(atob(token.split('.')[1]));

    this.can_search = payload.can_search || false;
    this.can_cancel = payload.can_cancel|| false;
    this.can_track = payload.can_track|| false;
  }
}
