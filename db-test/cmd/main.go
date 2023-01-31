package main

import (
	"fmt"

	"github.com/kkanyo/go_study/internal/database"
)

func main() {
	run()
}

func run() {
	db := database.DBConnect("mydb")
	defer db.Close()

	tableInfos, err := database.GetTables(db)
	if nil != err {
		panic(err)
	}

	for _, tableInfo := range tableInfos {
		fmt.Println("TABLE: ", tableInfo)

		columnInfos, err := database.GetColumnInfos(db, tableInfo)
		if nil != err {
			panic(err)
		}

		for _, columnInfo := range columnInfos {
			if columnInfo.Field.String != "" {
				fmt.Print(columnInfo.Field.String, ": ")
			} else {
				fmt.Print("NULL: ")
			}
			fmt.Println(columnInfo)
		}
	}

}
