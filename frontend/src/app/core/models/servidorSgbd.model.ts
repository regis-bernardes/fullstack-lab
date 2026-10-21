export interface ServidorSgbd {
  idServidor?: number;
  nomeServidor: string;
  host: string;
  porta: number;
  tipo: 'POSTGRES' | 'MYSQL' | 'ORACLE' | 'SQLSERVER';
  username: string;
  senhaCriptografada?: string;
  ativo: boolean;
  databaseName: string;
}

