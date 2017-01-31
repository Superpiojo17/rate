
<!DOCTYPE HTML>
<!--
	Retrospect by TEMPLATED
	templated.co @templatedco
	Released for free under the Creative Commons Attribution 3.0 license (templated.co/license)
-->
<html>
<head>

<meta charset="UTF-8">

<title>Login</title>

<meta name="viewport" content="width=device-width, initial-scale=1" />

<!--[if lte IE 8]><script src="assets/js/ie/html5shiv.js"></script><![endif]-->
<link rel="stylesheet" href="assets/css/main.css" />
<!--[if lte IE 8]><link rel="stylesheet" href="assets/css/ie8.css" /><![endif]-->
<!--[if lte IE 9]><link rel="stylesheet" href="assets/css/ie9.css" /><![endif]-->


<link
	href='http://fonts.googleapis.com/css?family=Titillium+Web:400,300,600'
	rel='stylesheet' type='text/css'>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/normalize/5.0.0/normalize.min.css">


<link rel="stylesheet" href="login/css/style.css">


</head>
<style> 
input[type=text] {
    width: 130px;
    float: right;
    box-sizing: border-box;
    border: 2px solid #ccc;
    border-radius: 4px;
    font-size: 16px;
    background-color: #1e2832;
    background-image: url('searchicon.png');
    background-position: 10px 10px; 
    background-repeat: no-repeat;
    padding: 12px 20px 12px 40px;
    -webkit-transition: width 0.4s ease-in-out;
    transition: width 0.4s ease-in-out;
}

input[type=text]:focus {
    width:50%;
}
</style>
<body class="landing">

	<!-- Header -->
	<header id="header" class="alt">
		<h1>
			<a href="index.html">Rate & Review</a>
		</h1>
		<a href="#nav">Menu</a>
		<form>
  <input type="text" name="search" placeholder="Search..">
</form>
	</header>

	<!-- Nav -->
	<nav id="nav">
		<ul class="links">
			<li><a href="index.html">Home</a></li>
			<li><a href="generic.html">Generic</a></li>
			<li><a href="elements.html">Elements</a></li>
		</ul>
	</nav>




	<!-- Four -->
	<section id="four" class="wrapper style2 special">
	
		<div class="inner">

			<div class="form">

				<ul class="tab-group">
					<li class="tab active"><a href="#login">Log In</a></li>
					<li class="tab"><a href="#signup">Sign Up</a></li>

				</ul>

				<div class="tab-content">


					<div id="login">
						<h1>Welcome Back!</h1>

						<form action="/" method="post">

							<div class="field-wrap">
								<label> Email Address<span class="req">*</span>
								</label> <input type="email" required autocomplete="off" />
							</div>

							<div class="field-wrap">
								<label> Password<span class="req">*</span>
								</label> <input type="password" required autocomplete="off" />
							</div>

							<p class="forgot">
								<a href="#">Forgot Password?</a>
							</p>

							<button class="button button-block" />
							Log In
							</button>

						</form>

					</div>
					<div id="signup">
						<h1>Sign Up for Free</h1>

						<form action="/" method="post">

							<div class="top-row">
								<div class="field-wrap">
									<label> First Name<span class="req">*</span>
									</label> <input type="text" required autocomplete="off" />
								</div>

								<div class="field-wrap">
									<label> Last Name<span class="req">*</span>
									</label> <input type="text" required autocomplete="off" />
								</div>
							</div>

							<div class="field-wrap">
								<label> Email Address<span class="req">*</span>
								</label> <input type="email" required autocomplete="off" />
							</div>

							<div class="field-wrap">
								<label> Set A Password<span class="req">*</span>
								</label> <input type="password" required autocomplete="off" />
							</div>

							<div class="field-wrap">
								<label> Confirm Password<span class="req">*</span>
								</label> <input type="password" required autocomplete="off" />
							</div>

							<!-- Form -->
							<!--  
							<section>
								<h3>Form</h3>
								<form method="post" action="#">
									<div class="row uniform 50%">
										<div class="6u 12u$(xsmall)">
											<input type="text" name="name" id="name" value=""
												placeholder="Name" />
										</div>
										<div class="6u$ 12u$(xsmall)">
											<input type="email" name="email" id="email" value=""
												placeholder="Email" />
										</div>
										<div class="12u$">
											<div class="select-wrapper">
												<select name="category" id="category">
													<option value="">- Category -</option>
													<option value="1">Manufacturing</option>
													<option value="1">Shipping</option>
													<option value="1">Administration</option>
													<option value="1">Human Resources</option>
												</select>
											</div>
										</div>
										<div class="4u 12u$(xsmall)">
											<input type="radio" id="priority-low" name="priority" checked>
											<label for="priority-low">Low Priority</label>
										</div>
										<div class="4u 12u$(xsmall)">
											<input type="radio" id="priority-normal" name="priority">
											<label for="priority-normal">Normal Priority</label>
										</div>
										<div class="4u$ 12u$(xsmall)">
											<input type="radio" id="priority-high" name="priority">
											<label for="priority-high">High Priority</label>
										</div>
										<div class="6u 12u$(small)">
											<input type="checkbox" id="copy" name="copy"> <label
												for="copy">Email me a copy of this message</label>
										</div>
										<div class="6u$ 12u$(small)">
											<input type="checkbox" id="human" name="human" checked>
											<label for="human">I am a human and not a robot</label>
										</div>
										<div class="12u$">
											<textarea name="message" id="message"
												placeholder="Enter your message" rows="6"></textarea>
										</div>
										<div class="12u$">
											<ul class="actions">
												<li><input type="submit" value="Send Message"
													class="special" /></li>
												<li><input type="reset" value="Reset" /></li>
											</ul>
										</div>
									</div>
								</form>
							</section>

-->
							<select>
								<option value="volvo"color="Black">Account Type</option>
								<option value="saab">Admin</option>
								<option value="opel">Teacher</option>
								<option value="audi">Tutor</option>
								<option value="audi">Student</option>
							</select>
							<br/>
							<button type="submit" class="button button-block"/>
							Get Started
							</button>
					</div>

				</div>
				<!-- tab-content -->

			</div>
			<!-- /form -->




		</div>
	</section>

	<!-- Footer -->
	<footer id="footer">
		<div class="inner">
			<ul class="icons">
				<li><a href="#" class="icon fa-facebook"> <span
						class="label">Facebook</span>
				</a></li>
				<li><a href="#" class="icon fa-twitter"> <span
						class="label">Twitter</span>
				</a></li>
				<li><a href="#" class="icon fa-instagram"> <span
						class="label">Instagram</span>
				</a></li>
				<li><a href="#" class="icon fa-linkedin"> <span
						class="label">LinkedIn</span>
				</a></li>
			</ul>
			<ul class="copyright">
				<li>&copy; Untitled.</li>
				<li>Images: <a href="http://unsplash.com">Unsplash</a>.
				</li>
				<li>Design: <a href="http://templated.co">TEMPLATED</a>.
				</li>
			</ul>
		</div>
	</footer>

	<!-- Scripts -->
	<script
		src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>

	<script src="login/js/index.js"></script>


	<script src="assets/js/jquery.min.js"></script>
	<script src="assets/js/skel.min.js"></script>
	<script src="assets/js/util.js"></script>
	<!--[if lte IE 8]><script src="assets/js/ie/respond.min.js"></script><![endif]-->
	<script src="assets/js/main.js"></script>

</body>
</html>