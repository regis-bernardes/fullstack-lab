import { Routes } from '@angular/router';
import { DashboardComponent } from './core/features/dashboard/dashboard.component';
import { SqlEditorComponent } from './core/features/sql-editor/sql-editor.component';
import { ServidorSgbdComponent } from './core/features/servers/servidorSgbd.component';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'servers/crud', component: ServidorSgbdComponent },
  { path: 'editor', component: SqlEditorComponent },
  { path: '**', redirectTo: 'dashboard' }
];