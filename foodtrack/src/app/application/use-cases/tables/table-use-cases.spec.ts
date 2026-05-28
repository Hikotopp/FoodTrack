import { of } from 'rxjs';
import { TableDashboard, TableSummary } from '../../../domain/entities/table.entity';
import { TablePort } from '../../../domain/ports/table.port';
import { AddOrderLineUseCase } from './add-order-line.use-case';
import { CloseOrderUseCase } from './close-order.use-case';
import { CreateTableUseCase } from './create-table.use-case';
import { DeleteTableUseCase } from './delete-table.use-case';
import { GetDashboardUseCase } from './get-dashboard.use-case';
import { ListTablesUseCase } from './list-tables.use-case';
import { RemoveOrderLineUseCase } from './remove-order-line.use-case';
import { UpdateOrderLineUseCase } from './update-order-line.use-case';
import { UpdateTableStatusUseCase } from './update-table-status.use-case';

describe('Table use cases', () => {
  let tablePort: jasmine.SpyObj<TablePort>;
  const summary: TableSummary = {
    id: 1,
    tableNumber: 4,
    status: 'AVAILABLE',
    total: 0,
    itemCount: 0
  };
  const dashboard: TableDashboard = {
    table: { id: 1, tableNumber: 4, status: 'OCCUPIED' },
    currentOrder: null,
    menuItems: []
  };

  beforeEach(() => {
    tablePort = jasmine.createSpyObj<TablePort>('TablePort', [
      'listTables',
      'getTableDashboard',
      'createTable',
      'deleteTable',
      'updateTableStatus',
      'addOrderLine',
      'updateOrderLine',
      'removeOrderLine',
      'closeOrder'
    ]);
  });

  it('ListTablesUseCase delegates to the table port', (done) => {
    tablePort.listTables.and.returnValue(of([summary]));

    new ListTablesUseCase(tablePort).execute().subscribe((result) => {
      expect(result).toEqual([summary]);
      expect(tablePort.listTables).toHaveBeenCalledOnceWith();
      done();
    });
  });

  it('GetDashboardUseCase delegates with the selected table id', (done) => {
    tablePort.getTableDashboard.and.returnValue(of(dashboard));

    new GetDashboardUseCase(tablePort).execute(1).subscribe((result) => {
      expect(result).toEqual(dashboard);
      expect(tablePort.getTableDashboard).toHaveBeenCalledOnceWith(1);
      done();
    });
  });

  it('CreateTableUseCase delegates with the table number', (done) => {
    tablePort.createTable.and.returnValue(of(summary));

    new CreateTableUseCase(tablePort).execute(4).subscribe((result) => {
      expect(result).toEqual(summary);
      expect(tablePort.createTable).toHaveBeenCalledOnceWith(4);
      done();
    });
  });

  it('DeleteTableUseCase delegates with the table id', (done) => {
    tablePort.deleteTable.and.returnValue(of(void 0));

    new DeleteTableUseCase(tablePort).execute(1).subscribe(() => {
      expect(tablePort.deleteTable).toHaveBeenCalledOnceWith(1);
      done();
    });
  });

  it('UpdateTableStatusUseCase delegates with the new status', (done) => {
    tablePort.updateTableStatus.and.returnValue(of({ ...summary, status: 'CLEANING' }));

    new UpdateTableStatusUseCase(tablePort).execute(1, 'CLEANING').subscribe((result) => {
      expect(result.status).toBe('CLEANING');
      expect(tablePort.updateTableStatus).toHaveBeenCalledOnceWith(1, 'CLEANING');
      done();
    });
  });

  it('AddOrderLineUseCase delegates with menu item and quantity', (done) => {
    tablePort.addOrderLine.and.returnValue(of(dashboard));

    new AddOrderLineUseCase(tablePort).execute(1, 20, 3).subscribe((result) => {
      expect(result).toEqual(dashboard);
      expect(tablePort.addOrderLine).toHaveBeenCalledOnceWith(1, 20, 3);
      done();
    });
  });

  it('UpdateOrderLineUseCase delegates with line and quantity', (done) => {
    tablePort.updateOrderLine.and.returnValue(of(dashboard));

    new UpdateOrderLineUseCase(tablePort).execute(1, 30, 2).subscribe((result) => {
      expect(result).toEqual(dashboard);
      expect(tablePort.updateOrderLine).toHaveBeenCalledOnceWith(1, 30, 2);
      done();
    });
  });

  it('RemoveOrderLineUseCase delegates with the line id', (done) => {
    tablePort.removeOrderLine.and.returnValue(of(dashboard));

    new RemoveOrderLineUseCase(tablePort).execute(1, 30).subscribe((result) => {
      expect(result).toEqual(dashboard);
      expect(tablePort.removeOrderLine).toHaveBeenCalledOnceWith(1, 30);
      done();
    });
  });

  it('CloseOrderUseCase delegates with the table id', (done) => {
    tablePort.closeOrder.and.returnValue(of(dashboard));

    new CloseOrderUseCase(tablePort).execute(1).subscribe((result) => {
      expect(result).toEqual(dashboard);
      expect(tablePort.closeOrder).toHaveBeenCalledOnceWith(1);
      done();
    });
  });
});
