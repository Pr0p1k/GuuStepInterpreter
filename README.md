# GuuStepInterpreter
JetBrains test assignment for Coroutines debugger

# Build
Execute *gradle jar* task. It will create **build/libs/guu-1.0.jar**

# Run
Execute *java -jar build/libs/guu-1.0.jar \<args\>* task to launch

Some example Guu files are in src/test/resources

Available args:
* -r - run without stops (straight execution of guu)
* -g - run with GUI. **Not implemented!**

Needs console to support *ANSI escape codes* to work correctly in colored mode

# Extension operators 
* inc \<varname\> - increments given variable
* read \<varname\> - reads value into given variable

# Extension commands 
* run - continues Guu execution without debugging
* exit - exits the interpreter
* help - prints list of available variables

# Description 
**Задача**: реализовать пошаговое исполнение кода для тривиального языка программирования Guu.

Внимание: в описании ниже заведомо опущены некоторые существенные моменты. Как правило, они остаются на ваше усмотрение. Если будет совсем непонятно, пишите на yan.zhulanow[at]jetbrains.com.

Программа на Guu состоит из набора процедур. Каждая процедура начинается со строки sub <subname> и завершается объявлением другой процедуры (или концом файла, если процедура в файле последняя). Исполнение начинается с sub main.

Тело процедуры – набор инструкций, каждая из которых находится на отдельной строке. В начале строки могут встречаться незначимые символы табуляции или пробелы. Пустые строки игнорируются. Комментариев в Guu нет.

В Guu есть лишь три оператора: - set <varname> <new value> – задание нового целочисленного значения переменной. - call <subname> – вызов процедуры. Вызовы могут быть рекурсивными. - print <varname> – печать значения переменной на экран.

Переменные в Guu имеют глобальную область видимости. Программа ниже выведет на экран строку a = 2.

sub main
    set a 1
    call foo
    print a

sub foo
    set a 2
А вот простейшая программа с бесконечной рекурсией:

sub main
   call main
Необходимо написать пошаговый интерпретатор для Guu. При его запуске отладчик должен останавливаться на строчке с первой инструкцией в sub main и ждать команд от пользователя. Минимально необходимый набор команд отладчика:

i – step into, отладчик заходит внутрь call <subname>.
o – step over, отладчик не заходит внутрь call.
trace – печать stack trace исполнения с номерами строк, начиная с main..
var – печать значений всех объявлённых переменных.
Формат общения пользователя с отладчиком остаётся на выше усмотрение. Вы можете выбрать как минималистичный GDB-like интерфейс, так и консольный или графический UI. Названия команд отладчика можно при желании изменить.

Для решения этой задачи вы можете использовать любой язык программирования из TIOBE TOP 50 и open-source компилятором/интерпретатором.

При оценке работы будет оцениваться:

Общая работоспособность программы;
Качество исходного кода и наличие тестов;
Простота расширения функциональности (например, поддержка новых операторов языка или инструкций отладчика).
