import 'package:flutter/material.dart';
import 'package:webview_flutter/webview_flutter.dart';

class SecondPage extends StatefulWidget {
  const SecondPage({Key? key}) : super(key: key);

  @override
  _SecondPageState createState() => _SecondPageState();
}

class _SecondPageState extends State<SecondPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text("测试一下X5 WebView"),
          centerTitle: true,
        ),
        body: Builder(builder: (BuildContext context) {
          return Container(
              child: WebView(initialUrl: 'https://mobile.baidu.com'));
          // return Container(
          //     child: WebView(initialUrl: 'http://debugtbs.qq.com'));
        }));
  }
}
