package object

import "github.com/kkanyo/go_study/internal/scheduler"

type Player struct {
	targetDistance uint32
	moveSpeed      float32

	scheduler.SchedulerRun
}

func (player *Player) SetTargerDistance(distance uint32) {
	player.targetDistance = distance
}

func (player *Player) SetMoveSpeed(speed float32) {
	player.moveSpeed = speed
}

func (player *Player) GetTargetDistance() uint32 {
	return player.targetDistance
}

func (player *Player) GetMoveSpeed() float32 {
	return player.moveSpeed
}
