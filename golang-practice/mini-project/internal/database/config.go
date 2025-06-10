package database

import (
	"io/ioutil"
	"log"

	"gopkg.in/yaml.v3"
)

type ConfigDB struct {
	Type     string `yaml:"database_type"`
	User     string `yaml:"database_user"`
	Password string `yaml:"database_password"`
	Host     string `yaml:"database_host"`
}

func LoadConfig(filePath string) ConfigDB {
	configFile, err := ioutil.ReadFile(filePath)
	if nil != err {
		log.Fatal(err)
	}

	var config ConfigDB
	err = yaml.Unmarshal(configFile, &config)
	if nil != err {
		log.Fatal(err)
	}

	return config
}
