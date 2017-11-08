/*
 * Quantis Sàrl CONFIDENTIAL
 * Unpublished Copyright (c) 2009-2017 Quantis SARL, All Rights Reserved.
 * NOTICE: All information contained herein is, and remains the property of Quantis Sàrl. The intellectual and
 * technical concepts contained herein are proprietary to Quantis Sàrl and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Quantis Sàrl. Access to the source code contained herein is hereby forbidden to anyone
 * except current Quantis Sàrl employees, managers or contractors who have executed Confidentiality and Non-disclosure
 * agreements explicitly covering such access.
 * The copyright notice above does not evidence any actual or intended publication or disclosure of this source code,
 * which includes information that is confidential and/or proprietary, and is a trade secret, of Quantis Sàrl.
 * ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC PERFORMANCE, OR PUBLIC DISPLAY OF OR THROUGH USE OF THIS SOURCE
 * CODE WITHOUT THE EXPRESS WRITTEN CONSENT OF Quantis Sàrl IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE LAWS
 * AND INTERNATIONAL TREATIES. THE RECEIPT OR POSSESSION OF THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY
 * OR IMPLY ANY RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT
 * IT MAY DESCRIBE, IN WHOLE OR IN PART.
 */
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

