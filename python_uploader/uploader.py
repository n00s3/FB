import os
import csv
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

cred = credentials.Certificate("./key/serviceAccountKey.json")
firebase_admin.initialize_app(cred)
db = firestore.client()

colls = {'정책할인':'policy',
    '딤채(3개채널)':'dimchae_3',
    '딤채':'dimchae',
    '프라우드':'praud',
    '딤채쿡':'dimchae_cook',
    '에어컨':'air',
    '냉장고, 냉동고':'ref',
    '세탁기':'wash',
    '기타소물':'etc',
    '전시품':'exhibition',
    '위니아전자':'winia',
    '20년형 딤채':'20dimchae'}
colls_list = ['policy', 'dimchae_3', 'dimchae', 'praud', 'dimchae_cook', 'air', 'ref', 'wash', 'etc', 'exhibition', 'winia', '20dimchae']

def select_collection():
    print('\n-------DB 목록-------')
    length = 0
    for i in colls:
        length += 1
        print('[{}] - {}'.format(length, i))

    print()
    idx = input('업로드 될 DB 번호를 선택해주세요(0은 종료): ')
    if int(idx) == 0:
        exit()

    if (int(idx) < 1) or (int(idx) > length):
        print('범위 초과\n\nUsage:\n업로드 될 DB 번호를 선택해주세요: 1')
        exit()

#    print(colls_list[int(idx)-1])
    return colls_list[int(idx)-1]

def select_csv():
    path = "./csv"
    file_list = os.listdir(path)
    file_list_csv = [file for file in file_list if file.endswith(".csv")]
    
    print('\n-------csv 목록-------')
    for i in range(len(file_list_csv)):
        print('[{}] - {}'.format(i+1, file_list_csv[i]))
    print()
    idx = input('업로드 할 파일의 인덱스 번호를 선택해주세요(0은 종료): ')

    if int(idx) == 0:
        exit()

    if (int(idx) < 1) or (int(idx) > len(file_list_csv)):
        print('범위 초과\n\nUsage:\n업로드 할 파일의 인덱스 번호를 선택해주세요: 1')
        exit()

    print(file_list_csv[int(idx)-1], '선택됨')
    return file_list_csv[int(idx)-1]



# check_csv(file_name):
# 첫 줄과 
# 두번재 줄이 9개여야함




# csv read
def read_csv(file_name):
    print('CSV 파일 형식 체크...')
    with open('./csv/' + file_name, 'r', encoding='utf8') as csv_file:
        csv_data = csv.reader(csv_file)
        datas = []

        cnt = 0
        for data in csv_data:
            cnt += 1
            if len(data) != 10:
                print(cnt,'-에러:', data)
                print('CSV 파일 형식이 올바르지 않습니다. 필드명과 필드값을 확인해주세요.')
                exit()
            datas.append(data)

    print('CSV 체크 완료')
    return datas[1:]

# def str_to_unicode(str):
#     unicode(str, "EUC-KR")


def delete_collection(coll_ref, batch_size):
    docs = coll_ref.stream()
    deleted = 0

    print('초기화 중...')
    for doc in docs:
        #print(u'Deleting doc {} => {}'.format(doc.id, doc.to_dict()))
        doc.reference.delete()
        deleted = deleted + 1
    print('초기화 완료!')

    if deleted >= batch_size:
        return delete_collection(coll_ref, batch_size)

def uploadFirebase(datas, coll_name):
    # fileds = ['model', 'category', 'channel', 'capacity', 'Grade', 'price', 'sale1', 'sale2', 'sale3', 'sale_price']

    #print(coll_ref)
    #delete_collection()
    delete_collection(db.collection(coll_name), 4)

    for i in range(len(datas)):
        # print(datas[i])
        data = {
            u'model': datas[i][0].strip(),
            u'category': datas[i][1].strip(),
            u'channel': datas[i][2].strip(),
            u'capacity': datas[i][3].strip(),
            u'Grade': datas[i][4].strip(),
            u'price': datas[i][5].strip(),
            u'sale1': datas[i][6].strip(),
            u'sale2': datas[i][7].strip(),
            u'sale3': datas[i][8].strip(),
            u'sale_price': datas[i][9].strip()
        }
        db.collection(coll_name).add(data)
        print(i+1, datas[i], '업로드 완료!')



# main
file_id = select_csv()
coll_name = select_collection()

datas = read_csv(file_id)
uploadFirebase(datas, coll_name)

print()
print(file_id, '업로드 완료')


