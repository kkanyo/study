package database

import (
	"database/sql"
	"log"

	_ "github.com/go-sql-driver/mysql"
)

func GetDBInfo() (driver, dir string) {
	config := LoadConfig("../bin/config-database.yaml")

	driver = config.Type
	dir = config.User + ":" + config.Password + "@tcp(" + config.Host + ")/"

	return
}

func DBConnect(dbName string) *sql.DB {
	driver, dir := GetDBInfo()

	db, err := sql.Open(driver, dir+dbName)
	if nil != err {
		log.Fatal(err)
	}

	return db
}
