package mydict

import "errors"

// struct가 아닌 map[string]string에 대한 alias(별명)
// Dictionary type
type Dictionary map[string]string

var (
	errNotFound   = errors.New("[Error: Not Found]")
	errWordExists = errors.New("[Error: Already exists]")
)

// Search for a word
func (d Dictionary) Search(word string) (string, error) {
	// 항상 에러 처리를 우선으로 작업
	value, exists := d[word]
	if exists {
		return value, nil
	}
	return "", errNotFound
}

// Add a word to the dictionary
func (d Dictionary) Add(word, def string) error {
	_, err := d.Search(word)
	switch err {
	case errNotFound:
		d[word] = def
	case nil:
		return errWordExists
	}
	// if err == errNotFound {
	// 	d[word] = def
	// } else if err == nil {
	// 	return errWordExists
	// }
	return nil
}

func (d Dictionary) Update(word, def string) error {
	_, err := d.Search(word)
	switch err {
	case nil:
		d[word] = def
	case errNotFound:
		return errNotFound
	}
	return nil
}

func (d Dictionary) Delete(word string) {
	delete(d, word)
}
