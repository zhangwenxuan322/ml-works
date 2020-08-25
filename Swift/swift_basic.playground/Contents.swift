/// the swift programming language swift 5.3 notes
/// zwx
/// 2020.07.15

// 处理可选型
// 1
var optionalName: String? = nil
var greeting = "Hello!"
if let name = optionalName {
    greeting = "Hello, \(name)"
}
if let firstNumber = Int("4"), let secondNumber = Int("42"), firstNumber < secondNumber && secondNumber < 100 {
    print("\(firstNumber) < \(secondNumber) < 100")
}
// Prints "4 < 42 < 100"

// 2
let nickname: String? = nil
let fullName: String = "John Appleseed"
let informalGreeting = "Hi \(nickname ?? fullName)"

// 函数作为返回值
func makeIncrementer() -> ((Int) -> Int) {
    func addOne(number: Int) -> Int {
        return 1 + number
    }
    return addOne
}
var increment = makeIncrementer()
increment(7)

// 函数作为参数
func hasAnyMatches(list: [Int], condition: (Int) -> Bool) -> Bool {
    for item in list {
        if condition(item) {
            return true
        }
    }
    return false
}
func lessThanTen(number: Int) -> Bool {
    return number < 10
}
var numbers = [20, 19, 7, 12]
hasAnyMatches(list: numbers, condition: lessThanTen)

// 闭包
numbers.map({ (number: Int) -> Int in
    if number > 10 {
        return number
    } else {
        return 0
    }
}).forEach { (i: Int) in
    print(i)
}

// 计算pi
let totalCount = 10000
var inCircleCount = 0
for _ in 0 ..< totalCount {
    let randomX = Double.random(in: 0 ... 1)
    let randomY = Double.random(in: 0 ... 1)
    let distance = randomX * randomX + randomY * randomY
    if distance <= 1 { inCircleCount += 1 }
}
// pi / 4 = inCircleCount / totalCount
let calPi = Double(inCircleCount) / Double(totalCount) * 4
