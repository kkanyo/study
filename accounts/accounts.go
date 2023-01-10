package accounts

import (
	"errors"
	"fmt"
)

// export 해주기 위해서는 comment 해주는 편이 좋음
// 또한 public하기 위해서 대문자로 시작하는 것 잊지 않기

// Account struct
type account struct {
	owner   string
	balance int
}

var errNEB = errors.New("[Error: Not enough balance]")

// 생성자가 없는 대신 함수로 대체
// NewAccount creates account
func NewAccount(owner string) *account {
	account := account{owner: owner, balance: 0}
	// 복사본을 반환하지 않음
	return &account
}

// method
// a: reciever를 통해 내부 변수 조작
// 포인터 객체로 전달받아야 함
// Deposit x amount on your account
func (a *account) Deposit(amount int) {
	a.balance += amount
}

// Balance of the acount
func (a *account) Balance() int {
	return a.balance
}

// Withdraw x amount on your account
func (a *account) Withdraw(amount int) error {
	// 예외 처리 기능이 지원되지 않음
	if a.balance < amount {
		return errNEB
	}

	a.balance -= amount
	// 예외가 없음을 반환
	return nil
}

// Change owner of the account
func (a *account) ChangeOwner(newOwner string) {
	a.owner = newOwner
}

// Owner of the account
func (a *account) Owner() string {
	return a.owner
}

// account를 출력할 때 형태를 결정
func (a account) String() string {
	return fmt.Sprint(a.Owner(), "'s account.\nHas: ", a.Balance())
}
