<div class="container">
    <div class="row">
	<div class="col-sm-6 pull-left LoginPageButtonCol">
            <button id = "userLoginButton" class ="btn btn-primary btn-lg LoginPageButton LoginPageButtonDark" type="button" data-toggle="modal" data-target="#userModal">Login</button>
        </div>
	<div class="col-sm-6 pull-right LoginPageButtonCol">
            <button id = "restaurantLoginButton" class = "btn btn-primary btn-lg LoginPageButton LoginPageButtonLight" type="button" data-toggle="modal" data-target="#restaurantModal">Restaurant Login</button>
	</div>
    </div>
    <div class="row">
	<div class="col-sm-6 pull-left LoginPageButtonCol">
            <button id = "userRegisterButton" class ="btn btn-primary btn-lg LoginPageButton LoginPageButtonLight" type="button" data-toggle="modal" data-target="#userModalReg">Register</button>
        </div>
	<div class="col-sm-6 pull-right LoginPageButtonCol">
            <button id = "restaurantRegisterButton" class = "btn btn-primary btn-lg LoginPageButton LoginPageButtonDark" type="button" data-toggle="modal" data-target="#restaurantModalReg">Restaurant Register</button>
	</div>
    </div>
	
	<!-- User login Modal -->
	<div class="modal fade" id="userModal" tabindex="-1" role="dialog" aria-labelledby="profileModal" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLongTitle"><span id="profileTitle">User Login</span></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
			  <div class="container-fluid">
			    <div class="row">
			      <div class="col-md-4"><h5>Username:</h5></div>
			      <div class="col-md-8"><textarea class="form-control" rows="1" id="userLogin"></textarea></div>
			    </div>
			    <div class="row">
			      <div class="col-md-4"><h5></h5></div>
			      <div class="col-md-8"><span id="profileDietary"></span></div>
			    </div>
			    <div class="row">
			      <div class="col-md-4"><h5>Password:</h5></div>
			      <div class="col-md-8"><input type="password" class="form-control" rows="1" id="userPassword"></div>
			    </div>
			  </div>
			</div>
      <div class="modal-footer">
	   <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="login()">Login</button>
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>
</div>