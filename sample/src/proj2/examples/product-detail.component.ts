import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {
  productId: number;
  product = { id: 0, name: '', description: '', price: 0 };

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.productId = +params.get('id');
      this.loadProductDetails();
    });
  }

  loadProductDetails() {
    // بيانات وهمية للمنتج
    const products = [
      { id: 1, name: 'منتج 1', description: 'وصف المنتج 1', price: 100 },
      { id: 2, name: 'منتج 2', description: 'وصف المنتج 2', price: 150 },
      { id: 3, name: 'منتج 3', description: 'وصف المنتج 3', price: 200 },
    ];
    this.product = products.find(p => p.id === this.productId) || this.product;
  }
}
