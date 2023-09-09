import { Component, EventEmitter, HostListener, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentPopupComponent } from '../departementpopup/department-popup.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-department-component',
  templateUrl: './department-custom.component.html',
  styleUrls: ['./department-custom.component.scss'],
})
export class DepartmentCustomComponent {
  @Input() isReadonly: boolean = false;
  @Input() placeholder: any;
  @Output() dbChanged = new EventEmitter<any>();
  @Input() departments?: IDepartment[] = [];

  constructor(protected modalService: NgbModal) {}

  /* ngAfterViewInit(): void {
    throw new Error('Method not implemented.');
  }

  ngOnInit(): void {
    throw new Error('Method not implemented.');
  } */

  /* onClickEvent(): void {
    this.onClickDiv.emit();
  } */

  onRemoveItem(event: any, item: any): void {
    event.stopPropagation();

    this.departments?.forEach((data, index) => {
      if (data.id === item.id) {
        this.departments?.splice(index, 1);
      }
    });
  }

  selectDepartment(): void {
    const modalRef = this.modalService.open(DepartmentPopupComponent, { size: 'lg', backdrop: 'static', centered: true });
    modalRef.componentInstance.passEntry.subscribe((data: IDepartment[]) => {
      data.forEach((d: IDepartment) => {
        if (!this.departments?.find(o => o.id === d.id)) {
          this.departments?.push(d);
        }
      });
    });
  }

  emitData(): void {
    this.dbChanged.emit(this.departments);
  }

  onClick(event: Event): void {
    if (this.isReadonly) {
      event.preventDefault();
      event.stopPropagation();
    }
  }
}
