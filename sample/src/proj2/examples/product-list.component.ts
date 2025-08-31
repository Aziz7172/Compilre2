import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent {
  products = [
    { id: 1, name: 'منتج 1', description: 'وصف المنتج 1', price: 100 },
    { id: 2, name: 'منتج 2', description: 'وصف المنتج 2', price: 150 },
    { id: 3, name: 'منتج 3', description: 'وصف المنتج 3', price: 200 },
  ];

  constructor(private router: Router) {}

  viewProductDetail(productId: number) {
    // عند النقر على المنتج ننتقل إلى صفحة تفاصيل المنتج
    this.router.navigate(['/products', productId]);
  }
}
