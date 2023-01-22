package main

import (
	"fmt"

	"github.com/kkanyo/go_study/internal/database"
)

func main() {
	db := database.DBConnect("market_db")
	defer db.Close()

	colInfoList := database.GetColumnInfoList(db, "member")

	for _, colInfo := range colInfoList {
		fmt.Println(colInfo.Name, colInfo.Type)
	}
}
