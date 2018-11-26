import 'package:alcig/contactPage/contact_page.dart';
import 'package:alcig/faqPage/faq_page.dart';
import 'package:alcig/login/activateUserBox/activate_user_box.dart';
import 'package:alcig/login/resetPasswordBox/reset_password_box.dart';
import 'package:alcig/parametersPage/parameters_page.dart';
import 'package:alcig/toolPage/tool_page.dart';
import 'package:angular/angular.dart';
import 'package:angular_router/angular_router.dart';


@Component(selector: 'tool-main-page',
                   directives: const [RouterOutlet],
                   templateUrl: 'tool_main_page.html',
                   styleUrls: const [])
@RouteConfig(const [
                 const Route(path: 'tool',
                                     component: ToolPage,
                                     name: 'Tool',
                                     useAsDefault: true),
                 const Route(path: 'contactUs',
                                     component: ContactPage,
                                     name: 'ContactUs'),
                 const Route(path: 'faq',
                                     component: FaqPage,
                                     name: 'Faq'),
                 const Route(path: 'parameters',
                                     component: ParametersPage,
                                     name: 'Parameters'),
                 const Route(path: 'resetPassword/:validationCode',
                                     component: ResetPasswordBox,
                                     name: 'ResetPassword'),
                 const Route(path: 'activateAccount/:registrationCode',
                                     component: ActivateUserBox,
                                     name: 'ActivateAccount')
             ])
class ToolMainPage
{
}


