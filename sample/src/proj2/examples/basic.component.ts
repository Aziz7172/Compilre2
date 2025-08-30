import { Component } from '@angular/core';

@Component({
  selector: 'app-basic',
  templateUrl: './basic.component.html',
  styleUrls: ['./basic.component.css']
})
export class BasicComponent {
  title: string = 'My First Angular Component';
  message: string = 'Welcome to Angular!';
  count: number = 3;
}
