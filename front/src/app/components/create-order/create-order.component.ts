import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CreateOrderService } from 'src/app/services/create-order.service';
import { Dish2 } from 'src/app/models/dish';

@Component({
  selector: 'app-create-order',
  templateUrl: './create-order.component.html',
})
export class CreateOrderComponent implements OnInit {
  token = localStorage.getItem('jwt') || '';
  dishes: Dish2[] = [];
  quantities: { [dishId: number]: number } = {}; 
  scheduledTime: string = ''; 
  can_order = false;
  can_schedule = false;

  constructor(
    private createOrderService: CreateOrderService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.checkPermissions()
    this.getDishes();
  }

  getDishes() {
    if(!this.can_order){
      alert("u dont have permission")
      this.router.navigate(['/order-list']);
      return;
    }
    this.createOrderService.getDishes().subscribe((results) => {
      this.dishes = results;
      results.forEach((dish) => {
        this.quantities[dish.dishId] = 0;
      });
    });
  }

  incrementQuantity(dishId: number): void {
    if (this.quantities[dishId] !== undefined) {
      this.quantities[dishId]++;
    }
  }

  decrementQuantity(dishId: number): void {
    if (this.quantities[dishId] > 0) {
      this.quantities[dishId]--;
    }
  }

  isOrderValid(): boolean {
    return Object.values(this.quantities).some((quantity) => quantity > 0);
  }

  placeOrder(): void {
    const orderItems = Object.entries(this.quantities)
      .filter(([dishId, quantity]) => quantity > 0) 
      .map(([dishId, quantity]) => ({
        dishId: +dishId,
        quantity: quantity as number,
      }));
      if(this.scheduledTime !== ''){
        this.createOrderService.scheduleOrder(orderItems, this.token, this.scheduledTime,).subscribe(
          (response) => {
            alert('Order created successfully!');
            this.router.navigate(['/order-list']);
          },
          (error) => {
            this.router.navigate(['/order-list']);
          }
        );
      }else{
        this.createOrderService.createOrder(orderItems, this.token).subscribe(
          (response) => {
            this.router.navigate(['/order-list']);
          },
          (error) => {
            this.router.navigate(['/order-list']);
          }
        );
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

    this.can_order = payload.can_order || false;
    this.can_schedule = payload.can_schedule || false;
  }
}
