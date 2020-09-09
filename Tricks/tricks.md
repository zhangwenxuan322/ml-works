## 遇到的一些杂类问题合计

### Vim光标操作

列举一些常用的光标快捷操作：

- w 向前移动一个单词，b 向后移动一个单词，e 移动到下一个单词的词尾，ge 移动到上一个单词的词尾
- gg 移动到文件头，G 移动到文件尾，$ 移动到行头，^ 移动到行尾
- y 复制指定范围内的内容，如要复制全文：ggyG
- :行数，可前往指定行
- dw 从指定位置删到下个单词开头，daw 删除光标所在的单词，dd 删除整行
- u 撤销操作，在.vimrc中配置`nnoremap U <C-r>`可得到 U 反撤销的操作

### Vim搭建Python环境

- Vim和Python3

  如果用的是macOS，并且是系统自带的Vim，那么它默认是不支持Python3的，可以通过`vim --version | grep python`查看。

  那么首先就是要安装一个支持Python3的Vim，这里brew提供了支持，通过brew安装的Vim默认支持Python3，安装语句`brew install vim`。同时Mac自带的是Python2，所以还需要安装Python3，安装语句`brew install python3`。为了能让系统调用到我们后来通过brew安装的Vim和Python3，需要修改系统环境变量加载顺序:

  1. `sudo vim /etc/paths`
  2. 调整顺序，将`/usr/local/bin`调至第一顺位
  3. 重启终端
  4. 完成上述操作后即可用which命令查看调用的vim和python3来源

- Mac上Vim中内容复制到其他程序

  在.vimrc中配置：`set clipboard=unnamed`

- Vim中直接运行Python文件

  在.vimrc中配置如下内容：

  ```sh
  " Python相关
  filetype plugin on
  " 按 F5 执行当前 Python 代码"
  map <F5> :call PRUN()<CR>
  func! PRUN()
      exec "w"
      if &filetype == 'python'
          exec "!python3 %"
      endif
  endfunc
  ```

- Vim插件管理

  推荐使用vim-plug进行Vim的插件管理，安装步骤如下：

  ```shell
  mkdir ~/.vim/autoload/
  cd ~/.vim/autoload/
  wget https://raw.githubusercontent.com/junegunn/vim-plug/master/plug.vim
  ```

  https://raw.githubusercontent.com的域名被DNS污染了，被解析成无效IP，需要挂代理。

  安装完成后只需要在.vimrc中将插件写在如下代码块中：

  ```sh
  call plug#begin('~/.vim/plugged')
  "插件写在这里"
  "Plug 'xxx/xxx' 格式像这样" 
  call plug#end()
  ```

  保存后执行`:PlugInstall`即可自动安装插件

- 关于jedi-vim插件无法联想第三方库文件的问题

  这个问题真是困扰了我一个下午，查找了很多资料，本来以为是macOS的问题，结果看到官方的一个[issue](https://github.com/davidhalter/jedi/issues/1188)上面提到jedi-vim还不支持Python3.7的第三方库查询，但是这个issue是两年前提出的并且作者说会在4-5个月内完成，不知道为什么还不支持：）。不过如果按照我上面的操作是不会遇到这个问题的，因为brew默认安装Python3.8，我之前用的是Python3.7。

- 其他的好用插件

  1. indentLine：`Plug 'Yggdroot/indentLine'`缩进指示器，`:IndentLinesToggle`开启或关闭
  2. autopep8：`Plug 'tell-k/vim-autopep8'`代码格式化，`:Autopep8`运行
  3. vim-flake8：`pip3 install flake8` -> `Plug 'nvie/vim-flake8'`代码检查，F7调用
  4. auto-pairs：`Plug 'jiangmiao/auto-pairs'`自动补齐括号
  5. nerdcommenter：`Plug 'preservim/nerdcommenter'`快捷注释，`\cc`注释，`\cu`反注释，前面可以加行数，默认是一行

