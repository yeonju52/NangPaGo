# nangpago-data

`nangpago-data` 폴더는 **CSV 데이터**를 MySQL과 **ElasticSearch**에 업로드하는 작업을 자동화하는 스크립트 모음입니다. 이 프로젝트는 데이터 전처리, CSV 파일 형식 변환, 그리고 다양한 저장소로의 업로드 과정을 포함하고 있어 효율적인 데이터 처리와 전송을 보장합니다.

## 프로젝트 구성

### 주요 구성 요소

- **데이터 전처리**: CSV 데이터를 MySQL과 ElasticSearch에 적합한 형식으로 변환합니다.
- **CSV 업로드 스크립트**: 전처리된 CSV 데이터를 MySQL과 ElasticSearch에 업로드하는 자동화된 스크립트입니다.

### 폴더 구조

```yaml
.
├── README.md
├── create_ingredients_dictionary-legacy.py
├── create_recipe_cleaned_csv.ipynb
├── datasets
│   ├── ingredients_cleand.csv               # 전처리된 재료 데이터 (MySQL 용)
│   ├── ingredients_dictionary.json          # Bulk 형식 재료 데이터 (ElasticSearch 용)
│   ├── normalized_pattern.pkl               # 정규화된 레시피 모음 (`create_recipe_cleaned_csv.ipynb`에서 사용)
│   ├── recipe.json                          # 원본 데이터
│   └── recipe_cleaned.csv                   # 전처리된 데이터
├── elastic
│   └── ingredients_dictionary
├── mysql
│   └── ingredients_dictionary
├── py310
│   ├── bin
│   ├── include
│   ├── lib
│   ├── pyvenv.cfg
│   └── share
└── requirements.txt
```

- **`datasets/`**: 데이터 파일들이 포함되어 있으며, CSV 파일도 포함되어 있습니다.
- **`create_recipe_cleaned_csv.ipynb`**: 데이터 전처리 관련 Jupyter Notebook 스크립트입니다.
- **`elastic/`**: ElasticSearch에 데이터를 업로드하는 스크립트들이 포함되어 있습니다.
- **`mysql/`**: MySQL에 데이터를 업로드하는 스크립트들이 포함되어 있습니다.

## 1. 원본 데이터 전처리

- `create_recipe_cleaned_csv.ipynb` 파일을 열고 **Run All**을 실행합니다.
- 실행 후, `datasets/recipe_cleaned.csv` 파일이 생성됩니다.

## 2. MySQL 데이터 업로드

이 스크립트는 전처리된 데이터를 MySQL에 업로드합니다.

- 다음 명령어를 사용하여 MySQL 데이터베이스에 데이터를 업로드합니다:
    ```bash
    python3 init_csv2sql_ingredients_dictionary.py --host {DB_IP} --user {DB_USERNAME} --password {DB_PWD} --database {DB_NAME} --port {DB_PORT}
    ```

- 이 명령어는 MySQL 서버의 `ingredients_dictionary` 테이블에 데이터를 삽입합니다.

## 3. ElasticSearch 데이터 업로드

### ElasticSearch 폴더 구조

```yaml
elastic
└── ingredients_dictionary
    ├── create_bulk_type.py
    ├── ingredients_dictionary.json
    └── init_csv2es_ingredients_dictionary.sh
```

### 단계별 설명

1. **기존 데이터를 Bulk 형식으로 변환**
    - `elastic/ingredients_dictionary/` 폴더로 이동합니다.
    - 다음 명령어를 실행하여 데이터를 Bulk 형식으로 변환합니다:
      ```bash
      cd elastic/ingredients_dictionary/
      python3 create_bulk_type.py
      ```
    - 변환된 데이터는 `{ id, name }` 형식에서 Bulk 형식으로 바뀌며, 예시는 다음과 같습니다:
      ```json
      {
        "index": { "_index": "ingredients_dictionary", "_type": "_doc" }
      }
      {
        "ingredient_id": id,
        "name": name
      }
      ```

2. **환경 변수 설정**
    - ElasticSearch 서버 URI를 설정합니다:
      ```bash
      export ELASTICSEARCH_URI={ELASTICSEARCH_IP:ELASTICSEARCH_PORT} # MAC
      set ELASTICSEARCH_URI={ELASTICSEARCH_IP:ELASTICSEARCH_PORT} # Windows
      ```

3. **ElasticSearch에 인덱스 생성 후 데이터 업로드**
    - (선택 사항 1) 명령어를 통해 데이터 업로드:
      ```bash
      chmod +x init_index_ingredients_dictionary.sh
      ./init_index_ingredients_dictionary.sh
      # ElasticSearch에 데이터 업로드
      curl -XPOST "${ELASTICSEARCH_URI}/_bulk?pretty" -H 'Content-Type: application/json' --data-binary @ingredients_dictionary.json
      ```

    - (선택 사항 2) API를 이용하여 데이터 업로드:
      ```bash
      chmod +x init_index_ingredients_dictionary.sh
      ./init_index_ingredients_dictionary.sh
      # API를 통해 데이터 업로드
      # POST `/api/ingredient/es/bulk-upload/mysql` 호출
      ```