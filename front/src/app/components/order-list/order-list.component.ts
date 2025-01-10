import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { OrderService } from 'src/app/services/order.service';
import { Orderer } from 'src/app/models/orderer';
import { UserListService } from 'src/app/services/user-list.service';

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
})
export class OrderListComponent implements OnInit {
  orders: Orderer[] = [];
  searchForm: FormGroup;
  token = localStorage.getItem('jwt') || "";
  statuses = ['ORDERED', 'PREPARING', 'IN_DELIVERY', 'DELIVERED', 'CANCELED'];
  private intervalId: any;
  isAdmin: boolean = false;

  constructor(private orderService: OrderService, private fb: FormBuilder, private userService: UserListService) {
    this.searchForm = this.fb.group({
      status: [''],
      dateFrom: [''],
      dateTo: [''],
      userId: ['']
    });
  }

  ngOnInit(): void {
    this.loadAllOrders();
  }

  loadAllOrders(): void {
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
    const filters = this.searchForm.value;
    this.orderService.searchOrders(filters, this.token).subscribe(
      (orders) => {
        this.orders = orders;
      },
      (error) => {
        alert(error);
      }
    );
  }
  startAutoSearch(): void {
    this.intervalId = setInterval(() => {
      this.onSearch();
    }, 5000); // svakih 5 sekundi
  }
  checkAdminStatus(): void {
    this.isAdmin = this.userService.checkAdminStatus()
  }
  cancelOrder(orderId: number): void {
    this.orderService.cancelOrder(orderId, this.token).subscribe(
      () => {
        alert('Order has been canceled successfully.');
        this.loadAllOrders();
      },
      (error) => {
        alert('Failed to cancel the order: ' + error);
      }
    );
  }

}
