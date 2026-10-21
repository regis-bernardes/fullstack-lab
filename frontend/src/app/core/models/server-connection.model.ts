export interface ServerConnection {
  id?: number;
  name: string;
  host: string;
  port: number | string;
  sgbd: string;         // Ex: POSTGRESQL, MYSQL, H2, ORACLE
  databaseName: string; // Nome da base de dados
  jdbcUrl?: string;
  driverClassName?: string;
  username?: string;
  password?: string;
}