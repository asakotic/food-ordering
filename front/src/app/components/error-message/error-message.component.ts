import { Component, OnInit } from '@angular/core';
import { ErrorServiceService } from 'src/app/services/error-service.service';

@Component({
  selector: 'app-error-message',
  templateUrl: './error-message.component.html',
  styleUrls: ['./error-message.component.css']
})
export class ErrorMessageComponent implements OnInit {
  errors: any[] = [];
  currentPage = 0;
  pageSize = 10;
  totalPages = 0;
  isAdmin = false;

  constructor(private errorService: ErrorServiceService) { }

  ngOnInit(): void {
    this.loadErrors();
  }

  loadErrors(): void {
    this.errorService.getErrors(this.currentPage, this.pageSize).subscribe(
      (data) => {
        this.errors = data.content;
        this.totalPages = data.totalPages;
      },
      (error) => {
        alert('Error loading error history: ' + error);
      }
    );
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadErrors();
    }
  }
}