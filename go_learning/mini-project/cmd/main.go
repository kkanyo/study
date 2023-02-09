package main

import (
	"fmt"
	"time"

	"github.com/kkanyo/go_study/internal/database"
	"github.com/kkanyo/go_study/internal/object"
	"github.com/kkanyo/go_study/internal/scheduler"
)

func main() {
	player := &object.Player{}

	initFSM(player)

	var elapse time.Duration
	elapse = 0
	for {
		timeNow := time.Now()

		time.Sleep(time.Microsecond)

		elapse += time.Since(timeNow)

		for int64(elapse) > int64(float32(1000/60)*float32(time.Millisecond)) {
			player.SchedulerRun.Update(elapse.Milliseconds())
			elapse = 0
		}
	}
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

func initFSM(player *object.Player) {
	player.SetMoveSpeed(6)
	player.SetTargerDistance(100)

	player.SchedulerRun.Append(&scheduler.Ready{})
	player.SchedulerRun.Append(&scheduler.Run{
		TargetDistance: player.GetTargetDistance(),
		MoveSpeed:      player.GetMoveSpeed(),
	})

}
