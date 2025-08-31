import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.css']
})
export class AddProductComponent {
  product = {
    name: '',
    description: '',
    price: 0
  };

  constructor(private router: Router) {}

  addProduct() {
    // نعمل على إضافة المنتج (هنا نطبّق عملية وهمية)
    console.log('منتج تمت إضافته:', this.product);

    // بعد إضافة المنتج نعيد توجيه المستخدم إلى صفحة عرض المنتجات
    this.router.navigate(['/products']);
  }
}
