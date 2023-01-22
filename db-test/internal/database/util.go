package database

import (
	"database/sql"
	"log"
)

type ColumnInfo struct {
	Name string
	Type string
}

func GetTables(db *sql.DB) (tables []string) {
	rows, err := db.Query("SHOW TABLES")
	if nil != err {
		log.Fatal(err)
	}
	defer rows.Close()

	var table string
	for rows.Next() {
		err = rows.Scan(&table)
		if nil != err {
			log.Fatal(err)
		}
		//fmt.Println(table)
		tables = append(tables, table)
	}

	return
}

func GetColumnInfoList(db *sql.DB, table string) (colInfoList []ColumnInfo) {
	rows, err := db.Query("SELECT * FROM " + table)
	if nil != err {
		log.Fatal(err)
	}
	defer rows.Close()

	columnTypes, err := rows.ColumnTypes()
	if nil != err {
		log.Fatal(err)
	}

	for _, columnType := range columnTypes {
		var colInfo ColumnInfo
		colInfo.Name = columnType.Name()
		colInfo.Type = columnType.DatabaseTypeName()

		colInfoList = append(colInfoList, colInfo)
	}

	return
}
