package main

import (
	"fmt"
	"log"
	"net/http"
	"strconv"

	"github.com/PuerkitoBio/goquery"
)

type courseSkill struct {
	level      string
	categories string
	skills     string
}

var baseURL string = "https://www.inflearn.com/courses?order=seq"

func main() {
	tootalPages := getPages()

	for i := 0; i < tootalPages; i++ {
		getPage(i)
	}
}

func getPage(page int) {
	pageURL := baseURL + "&page=" + strconv.Itoa(page)
	// fmt.Println("Requesting", pageURL)

	res, err := http.Get(pageURL)
	checkErr(err)
	checkCode(res)

	// 메모리 릭 방지
	defer res.Body.Close()

	doc, err := goquery.NewDocumentFromReader(res.Body)
	checkErr(err)

	doc.Find(".back_course_matas").Each(func(i int, backCard *goquery.Selection) {
		level := backCard.Find(".course_level>span").Text()
		fmt.Println(level)
	})

}

func getPages() int {
	pages := 0
	res, err := http.Get(baseURL)
	checkErr(err)
	checkCode(res)

	// 메모리 릭 방지
	defer res.Body.Close()

	doc, err := goquery.NewDocumentFromReader(res.Body)
	checkErr(err)

	doc.Find(".pagination_container").Each(func(i int, s *goquery.Selection) {
		pages = s.Find("a").Length()
		// fmt.Println(pages)
	})

	return pages
}

func checkErr(err error) {
	if err != nil {
		log.Fatalln(err)
	}
}

func checkCode(res *http.Response) {
	if res.StatusCode != 200 {
		log.Fatalln("Request failed with Status:", res.StatusCode)
	}
}
