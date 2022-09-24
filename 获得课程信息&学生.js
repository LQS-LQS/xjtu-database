import fetch from "node-fetch";
import fs from 'fs'


//写入的课程txt
const writeStream_COURSE = fs.createWriteStream('C:\\Users\\AIERXUAN\\Desktop\\111COURSE.txt', {
  encoding: "utf8",
  highWaterMark: 1,
})
//写入的学生txt
const writeStream_Student = fs.createWriteStream('C:\\Users\\AIERXUAN\\Desktop\\111STUDENT.txt', {
  encoding: 'utf-8',
  highWaterMark: 1,
})

const familyName = ['张', '王', '李', '赵', '刘', '孙', '何', '周', '郑', '王', '冯', '陈', '朱', '秦', '苏', '范', '霍'];
const Cookie = 'EMAP_LANG=zh; THEME=cherry; _WEU=K8d4YPaxIg3wO0XViBBzGjR5CGSGpWsWgTnRiAmUH0K4fEzsJwGaQJGttZ1pwR_nhJXxAM5JjFTfm9ZuROkf7v45yZHyk7pRT27xV17M6Qo.; _ga=GA1.3.1272881.1647839865; loginServiceserchVal=%u6210%u7EE9; loginServiceclassifyId=all; loginServiceroleId=all; loginServiceSearchVal=; openLoginServicePageFlag=false; CASTGC=CRwfCZVfsmIjIs/uJQ/q10ZRRDA5clLm0YSsEOxYCRFLIFBqVfxAlg==; MOD_AMP_AUTH=MOD_AMP_1f7f8a40-941c-4697-9dfc-61ab353f5e2c; route=8b9256afce303b2bfefa79284f0e767c; asessionid=72429214-8350-4198-9c71-816ecf8bd473; amp.locale=undefined; JSESSIONID=TGIp4RUalG48ivjlR067B10CWXpxmyE_WG66IbmRm0iy9bAB1Vsh!-1678842815';
let randomNumer = 6;
let COURSE_DEPARTEMT = new Map();
COURSE_DEPARTEMT.set('人文社会科学学院', 'RW').set('医学部', 'YX').set('电气工程学院', 'DQ')
  .set('马克思主义学院', 'MK').set('机械工程学院', 'JX').set('化学学院', 'HX')
  .set('法学院', 'FX').set('生命科学与技术学院', 'SM').set('经济与金融学院', 'JJ')
  .set('物理学院', 'WL').set('管理学院', 'GL').set('人居环境与建筑工程学院', 'RJ')
  .set('外国语学院', 'WG').set('公共政策与管理学院', 'GG').set('体育中心', 'TY')
  .set('能源与动力工程学院', 'ND').set('金禾经济研究中心', 'JH').set('化学工程与技术学院', 'HX')
  .set('数学与统计学院', 'SX').set('实践教学中心/工程坊', 'SB').set('航天航空学院', 'HK')
  .set('军事教研室', 'JS').set('材料科学与工程学院', 'CL').set('国际教育学院', 'GJ')
  .set('新闻与新媒体学院', 'XW').set('钱学森学院/钱学森书院', 'QX').set('学工部/学生处/武装部', 'XG')
  .set('前沿科学技术研究院', 'QY').set('学生就业创业指导服务中心', 'JY').set('教务处', 'JW');

let COURSE_NAME = [];
let COURSE_NUM = [];
let STUDENT_NAME = '';
let pageNumber = 1;
async function sendPostToGetInfo() {

  //发异步的fetch请求
  const res = await fetch("http://ehall.xjtu.edu.cn/jwapp/sys/kcbcx/modules/qxkcb/qxfbkccx.do", {
    method: 'POST',
    mode: 'cors', // no-cors, *cors, same-origin
    cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
    credentials: "include", // include, *same-origin, omit
    headers: {
      Accept: 'application/json, text/javascript, */*; q=0.01',
      "Accept-Encoding": 'gzip, deflate',
      Host: 'ehall.xjtu.edu.cn',
      Referer: 'http://ehall.xjtu.edu.cn/jwapp/sys/kcbcx/*default/index.do?amp_sec_version_=1&gid_=TmRDa3NtVm5tNGdQd1V6YThNQUFndFplUzdaREJHblRnR3VuNEFlaVJ4bFp1am1ScUxybisxTTVtR2p3NGNEQkFLS1A4bUJkQ09pL0ZrODZzd3JXeGc9PQ&EMAP_LANG=zh&THEME=cherry',
      'Content-type': 'application/x-www-form-urlencoded; charset=UTF-8',
      Cookie: Cookie,
      'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36',
    },
    body: `querySetting=[{"name":"XNXQDM","value":"2021-2022-2","linkOpt":"and","builder":"equal"},[{"name":"RWZTDM","value":"1","linkOpt":"and","builder":"equal"},{"name":"RWZTDM","linkOpt":"or","builder":"isNull"}]]&*order=+KKDWDM,+KCH,+KXH&SKXQ=&KSJC=&JSJC=&pageSize=10000&pageNumber=${pageNumber++}`
  })

  //解析成json数据格式
  const body = await res.json();
  console.log(body);
  let courseArray = body.datas.qxfbkccx.rows;


  //参数的意思
  /**
    * @SKBJ上课班级
    * @SKJS上课教师
    * @KCM课程名称
    * @KCH课程号
    * @YPSJDD上课时间地点
    * @XS学时
    * @XF学分
    * @KKDWDM_DISPLAY开课单位
 */

  courseArray.forEach(item => {

    //判断为null  undefined 包含空格(说明这是外教)  
    if (!(item.SKJS == null || item.SKJS == undefined || item.SKJS.includes(' ')))
      STUDENT_NAME = STUDENT_NAME.concat(item.SKJS + ',')

    let cno = COURSE_DEPARTEMT.get(item.KKDWDM_DISPLAY);
    if (cno == undefined) {//确定是EE还是CS
      //CS的课
      if ((item.KCM != null && item.KCM.indexOf('计算机') != -1) || (item.SKBJ != null && item.SKBJ.indexOf('计算机') != -1)) cno = 'CS';
      else cno = 'EE';
    }

    //之前没有找到并且课程名字不包括（
    if (COURSE_NAME.includes(item.KCM) == false && item.KCM.includes('（') == false) {
      COURSE_NAME.push(item.KCM.slice(0, 18));
      COURSE_NUM.push(cno + '-' + randomNumer);
      randomNumer++;
    }


    //上课班级
    let SKBJ = '',
      indexOfSKBJ;

    if (item.SKBJ == null) {
      SKBJ = '所有';
    } else {
      indexOfSKBJ = item.SKBJ.indexOf(',');
      if (indexOfSKBJ == -1) SKBJ = item.SKBJ;
      else SKBJ = item.SKBJ.slice(0, indexOfSKBJ);
    }

    //上课教师
    let SKJS = '',
      indexOfSKJS;

    if (item.SKJS == null) {
      SKJS = '不知道';
    } else {
      indexOfSKJS = item.SKJS.indexOf(',');
      if (indexOfSKJS == -1) SKJS = item.SKJS;
      else SKJS = item.SKJS.slice(0, indexOfSKJS);
    }

  })
}

await sendPostToGetInfo();
await sendPostToGetInfo();
await sendPostToGetInfo();
await sendPostToGetInfo();


console.log(COURSE_NAME);
//把课程写到文件里面去
for (let i = 0; i < COURSE_NUM.length; i++) {
  let cname = COURSE_NAME[i];
  let cno = COURSE_NUM[i];

  for (let j = 0; j < cname.length; j++) writeStream_COURSE.write(cname[j]);
  writeStream_COURSE.write('#$');
  for (let j = 0; j < cno.length; j++) writeStream_COURSE.write(cno[j]);
  writeStream_COURSE.write('\n');
}

//STUDENT_NAME是字符串，将字符串通过逗号划分为数组STUDENT_NAME_ARRAY
let STUDENT_NAME_ARRAY = STUDENT_NAME.split(',');

//先去重一次
let tmpSet = new Set(STUDENT_NAME_ARRAY);
STUDENT_NAME_ARRAY = [...tmpSet];

//随机生成不同的名字
let initLength = STUDENT_NAME_ARRAY.length;
for (let i = 0; i < initLength; i++) {
  let ranNum = Math.round(Math.random() * (familyName.length - 1));
  STUDENT_NAME_ARRAY.push(familyName[ranNum] + STUDENT_NAME_ARRAY[i].slice(1))
  ranNum = Math.round(Math.random() * (familyName.length - 1));
  STUDENT_NAME_ARRAY.push(familyName[ranNum] + STUDENT_NAME_ARRAY[i].slice(1))
}
STUDENT_NAME_ARRAY = STUDENT_NAME_ARRAY.sort((a, b) => Math.random() - 0.5);

let STUDENT_NAME_ARRAY_UNIQUE = new Set(STUDENT_NAME_ARRAY)

let fuck = [...STUDENT_NAME_ARRAY_UNIQUE]
let ssstr = fuck.join(',');

for (let i = 0; i < ssstr.length; i++) {
  let item = ssstr[i];
  if (item == ',') writeStream_Student.write('\n');
  else writeStream_Student.write(item);
}
