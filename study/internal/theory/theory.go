package theory

// formatting을 위한 패키지
import (
	"errors"
	"fmt"
	"log"
	"net/http"
	"strings"
	"time"

	"github.com/kkanyo/learngo/accounts"
	"github.com/kkanyo/learngo/mydict"
	"github.com/kkanyo/learngo/something"
)

// func 함수명(매개변수 타입, ... ) [반환 타입] {}
func multiply(a int, b int) int {
	return a * b
}

// 반환 타입을 여러 개 설정 가능
func lenAndUpper(name string) (int, string) {
	return len(name), strings.ToUpper(name)
}

// 매개변수의 수를 지정하지 않아도 가능 (하나의 타입만)
func repeatMe(words ...string) {
	fmt.Println(words)
}

// 반환 값의 이름을 명시할 수 있음
func lenAndUpper2(name string) (length int, uppercase string) {
	// function이 끝났을 때의 발생할 이벤트 예약
	defer fmt.Println("Done...")

	// := 를 사용하여 새로 생성하는 것이 아님
	length = len(name)
	uppercase = strings.ToUpper(name)
	return
}

// for 문
func superAdd(numbers ...int) int {
	for index, number := range numbers {
		fmt.Println(index, number)
	}

	total := 0
	for _, number := range numbers {
		total += number
	}

	for i := 0; i < len(numbers); i++ {
		fmt.Println(numbers[i])
	}
	return total
}

func canIDrink(age int) bool {
	// 조건을 검사하기 전에 변수 생성 가능
	// if 문을 빠져나오면 해제
	if koreanAge := age + 2; koreanAge < 18 {
		return false
	}
	return true
}

func canIDrink2(age int) bool {
	switch koreanAge := age + 2; koreanAge {
	case 10:
		return false
	case 18:
		return true
	}

	// if 문처럼 사용 가능
	switch {
	case age < 18:
		return false
	case age == 18:
		return true
	case age > 50:
	}
	return false
}

// Go는 class가 존재하지 않음
// struct로 모든 것을 대체 (생성자의 개념도 없음)
// 함수를 포함할 수 있음 (생성자 직접 구현)
type person struct {
	name    string
	age     int
	favFood []string
}

func test() {
	fmt.Println("Hello world!")

	// function을 export하기 위해서는 대문자로 호출
	something.SayHello()
	// private function이므로 사용 불가 (소문자로 시작)
	//something.sayBye()

	fmt.Println("\n---About variable---")
	const name1 string = "kkanyo" // 사용하지 않아도 됨
	var name2 = "kyabetsu"        // 변수 이름 우측에 타입 명시적 지정 가능
	name3 := "kkanyo"             // func 밖에서는 동작 X, 오직 변수에만 적용 가능
	fmt.Println(name2)
	fmt.Println(name3)

	// function
	fmt.Println("\n---About function---")
	fmt.Println(multiply(2, 2))
	totalLength, upperName := lenAndUpper("kkanyo")
	totalLength2, _ := lenAndUpper("kyabetsu") // _를 사용하여 반환 값 일부 무시 가능
	fmt.Println(totalLength, upperName)
	fmt.Println(totalLength2)

	repeatMe("kkanyo", "kyabetsu", "C++", "Game")

	totalLength3, upperName3 := lenAndUpper2("kkanyo")
	fmt.Println(totalLength3, upperName3)

	// for
	fmt.Println("\n---About for---")
	resultAdd := superAdd(1, 2, 3, 4, 5, 6)
	fmt.Println(resultAdd)

	// if
	fmt.Println("\n---About if---")
	fmt.Println(canIDrink(16))

	// switch
	fmt.Println("\n---About switch---")
	fmt.Println(canIDrink2(16))

	// pointer
	fmt.Println("\n---About pointer---")
	a := 2
	b := a // copy to value
	a = 10
	fmt.Println(a, b)
	fmt.Println(&a, &b)
	c := &a // copy address
	fmt.Println(&a, c)
	fmt.Println(&a, *c)
	a = 20
	fmt.Println(a, *c)
	*c = 30
	fmt.Println(a, *c)

	// array
	fmt.Println("\n---About array---")
	names := [5]string{"kkanyo", "kaybetsu"}
	names[2] = "C++"
	names[3] = "Game"
	names[4] = "Server"
	fmt.Println(names)
	// 동적 크기 배열
	names2 := []string{"kkanyo", "kyabetsu"}
	// append는 배열을 slice 배열과 새로운 slice를 반환
	names2 = append(names2, "C++")
	names2 = append(names2, "Game")
	// index out of range 런타임 에러 발생
	// names2[4] = "Server"
	fmt.Println(names2)

	// map
	fmt.Println("\n---About map---")
	kkanyo := map[string]string{"name": "kkanyo", "age": "27"}
	fmt.Println(kkanyo)

	for _, value := range kkanyo {
		fmt.Println(value)
	}
	for key, _ := range kkanyo {
		fmt.Println(key)
	}

	// struct
	fmt.Println("\n---About struct---")
	favFood := []string{"Chiken", "Yakisoba"}
	kkanyo2 := person{name: "kkanyo", age: 27, favFood: favFood}
	fmt.Println(kkanyo2.name)
}

func testAccountDictionary() {
	// 1. ACCOUNT EXAMPLE
	fmt.Println("----- ACCOUNT EXAMPLE -----")
	account := accounts.NewAccount("kkanyo")
	// 참조 형식을 반환하기 때문에 &가 붙음
	fmt.Println(account)

	// package가 아닌 변수를 통해 호출
	account.Deposit(10)
	fmt.Println(account.Balance())

	err := account.Withdraw(20)
	if err != nil {
		// 에러 메세지 출력
		log.Println(err)
	}
	fmt.Println(account.Balance())

	fmt.Println(account.Balance(), account.Owner())

	// 2. DICTIONARY EXAMPLE
	fmt.Println("\n\n----- DICTIONARY EXAMPLE -----")
	dictionary := mydict.Dictionary{"first": "First word"}
	dictionary["hello"] = "hello"
	fmt.Println(dictionary)

	definition, err := dictionary.Search("second")
	if err != nil {
		fmt.Println(err)
	} else {
		fmt.Println(definition)
	}

	definition, err = dictionary.Search("first")
	if err != nil {
		fmt.Println(err)
	} else {
		fmt.Println(definition)
	}

	err = dictionary.Add("hi", "Greeting")
	if err != nil {
		fmt.Println(err)
	}
	definition, err = dictionary.Search("hi")
	if err != nil {
		fmt.Println(err)
	} else {
		fmt.Println(definition)
	}

	err = dictionary.Add("hello", "Greeting")
	if err != nil {
		fmt.Println(err)
	}

	err = dictionary.Update("hello", "Second")
	if err != nil {
		fmt.Println(err)
	} else {
		fmt.Println(dictionary["hello"])
	}

	fmt.Println(dictionary)
	dictionary.Delete("hello")
	fmt.Println(dictionary)
}

// URL CHECKER & GO ROUINTES
type reqResult struct {
	url    string
	status string
}

var (
	errRequestFailed = errors.New("Request failed")
)

func test3() {
	// ----- GOROUTINE -----
	fmt.Println("\n----- GOROUTINE -----a")
	// 동시성(Concurrency)
	// 한 번 작업 때의 호출 순서는 랜덤함
	// 프로그램이(메인함수가) 실행되는 동안만 유효함
	go sexyCount("kkanyo")
	go sexyCount("kyabetsu")
	sexyCount("Olatte")
	// ... 상위 명령도 go 키워드라면 더 이상 작업 내역이 없으므로
	// main 함수가 종료되고, goroutine들이 소멸됨
	// 즉, go는 main 함수가 일감을 나눠주는 느낌

	// ----- Channel -----
	// gorouinte 사이의 커뮤니케이션 방법
	// 메세지 파이프와 같음
	// 채널의 데이터 타입을 명시해주어야 함
	fmt.Println("\n----- Channel -----")
	people := [2]string{"kkanyo", "kyabetsu"}

	c := make(chan string)
	for _, person := range people {
		go isSexy(person, c)
	}
	fmt.Println("Waiting for messages")
	// Blocking operation
	// 연산의 결과가 나올 때까지
	// 메세지를 전부 받지 않아도 됨
	for i := 0; i < len(people); i++ {
		fmt.Println(<-c)
	}
	// Deadlock
	// grouinte의 개수보다 많은 메세지를 기다리는 경우
	// 메세지를 계속 기다리고 있지만, goroutine은 끝난 상태
	//fmt.Println(<-c)

	// ----- URL CHECK -----
	fmt.Println("\n----- URL CHECK -----")
	// var results = map[string]string{}
	// make(): map 생성 함수 (빈 map을 사용할 땐 반드시 make함수 사용)
	results := make(map[string]string)
	c2 := make(chan reqResult)

	urls := []string{
		"https://www.airbnb.com/",
		"https://www.google.com/",
		"https://www.amazon.com/",
		"https://www.reddit.com/",
		"https://www.google.com/",
		"https://soundcloud.com/",
		"https://www.facebook.com/",
		"https://www.instagram.com/",
		"https://academy.nomadcoders.co/",
	}

	// panic 발생: 초기화되지 않은 map에는 삽입 불가
	// results["hello"] = "hello"

	for _, url := range urls {

		go hitURL(url, c2)
	}

	for i := 0; i < len(urls); i++ {
		result := <-c2
		results[result.url] = result.status
	}

	for url, status := range results {
		fmt.Println(url, status)
	}
}

// chan<- 경우 데이터를 보낼 수만 있음
func hitURL(url string, c chan<- reqResult) {
	resp, err := http.Get(url)
	status := "OK"
	// 300까지는 리다이렉션을 해주고,
	// 400부터는 문제 발생
	if err != nil || resp.StatusCode >= 400 {
		status = "FAILED"
	}
	c <- reqResult{url: url, status: status}
}

func sexyCount(person string) {
	for i := 0; i < 5; i++ {
		fmt.Println(person, "is sexy", i)
		time.Sleep(time.Second)
	}
}

// channel을 인자로 받아옴
func isSexy(person string, c chan string) {
	time.Sleep(time.Second * 5)
	// <- 연산자를 통해 채널에 값을 전달
	c <- person + " is sexy"
}
