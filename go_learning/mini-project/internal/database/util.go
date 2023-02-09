package database

import (
	"database/sql"
)

type ColumnInfo struct {
	Field      sql.NullString
	Type       sql.NullString
	Collation  sql.NullString
	Null       sql.NullString
	Key        sql.NullString
	Default    sql.NullString
	Extra      sql.NullString
	Privileges sql.NullString
	Comment    sql.NullString
}

func GetTables(db *sql.DB) (tables []string, err error) {
	rows, err := db.Query("SHOW TABLES")
	if nil != err {
		return nil, err
	}
	defer rows.Close()

	var table string
	for rows.Next() {
		err = rows.Scan(&table)
		if nil != err {
			return nil, err
		}
		//fmt.Println(table)
		tables = append(tables, table)
	}

	return
}

func GetColumnInfos(db *sql.DB, tableName string) (columnInfos []ColumnInfo, err error) {
	// 이 방법의 경우 Column의 다른 속성을 가져오기 어려움
	// 메타데이터만 알고 싶은 경우, LIMIT 1을 통해 하나의 레코드만 불러옴
	// rows, err := db.Query("SELECT * FROM " + tableName + " LIMIT 1")
	rows, err := db.Query("SHOW FULL COLUMNS FROM " + tableName)
	if nil != err {
		return nil, err
	}
	defer rows.Close()

	for rows.Next() {
		columnInfo := ColumnInfo{}
		err = rows.Scan(&columnInfo.Field,
			&columnInfo.Type,
			&columnInfo.Collation,
			&columnInfo.Null,
			&columnInfo.Key,
			&columnInfo.Default,
			&columnInfo.Extra,
			&columnInfo.Privileges,
			&columnInfo.Comment)
		if nil != err {
			return nil, err
		}
		//fmt.Println(table)
		columnInfos = append(columnInfos, columnInfo)
	}

	return
}
