package main

import (
	"fmt"
	"time"

	"github.com/kkanyo/go_study/internal/database"
	"github.com/kkanyo/go_study/internal/object"
	"github.com/kkanyo/go_study/internal/scheduler"
	slice "github.com/kkanyo/go_study/internal/util"
)

func testDB() {
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

func testFSM() {
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

func testSlice() {
	testSlice := make([]uint32, 0)

	fmt.Println("slice:", testSlice)
	fmt.Println("len:", slice.GetLength(testSlice))
	fmt.Println("cap:", slice.GetCapicity(testSlice))

	slice.Add(&testSlice, 1)

	fmt.Println("slice:", testSlice)
	fmt.Println("len:", slice.GetLength(testSlice))
	fmt.Println("cap:", slice.GetCapicity(testSlice))

	slice.Add(&testSlice, 2)

	fmt.Println("slice:", testSlice)
	fmt.Println("len:", slice.GetLength(testSlice))
	fmt.Println("cap:", slice.GetCapicity(testSlice))

	slice.Add(&testSlice, 3)

	fmt.Println("slice:", testSlice)
	fmt.Println("len:", slice.GetLength(testSlice))
	fmt.Println("cap:", slice.GetCapicity(testSlice))

	slice.Remove(&testSlice, 0)

	fmt.Println("slice:", testSlice)
	fmt.Println("len:", slice.GetLength(testSlice))
	fmt.Println("cap:", slice.GetCapicity(testSlice))

	slice.Init(&testSlice, 3)

	fmt.Println("slice:", testSlice)
	fmt.Println("len:", slice.GetLength(testSlice))
	fmt.Println("cap:", slice.GetCapicity(testSlice))

	slice.Copy(&testSlice, []uint32{1, 2, 3, 4, 5})

	fmt.Println("slice:", testSlice)
	fmt.Println("len:", slice.GetLength(testSlice))
	fmt.Println("cap:", slice.GetCapicity(testSlice))

	slice.Clear(&testSlice)

	fmt.Println("slice:", testSlice)
	fmt.Println("len:", slice.GetLength(testSlice))
	fmt.Println("cap:", slice.GetCapicity(testSlice))

	slice.CopyKeepCapicity(&testSlice, []uint32{1, 2, 3})

	fmt.Println("slice:", testSlice)
	fmt.Println("len:", slice.GetLength(testSlice))
	fmt.Println("cap:", slice.GetCapicity(testSlice))

	slice.CopyKeepCapicity(&testSlice, []uint32{1, 2, 3, 4, 5, 6})

	fmt.Println("slice:", testSlice)
	fmt.Println("len:", slice.GetLength(testSlice))
	fmt.Println("cap:", slice.GetCapicity(testSlice))
}
